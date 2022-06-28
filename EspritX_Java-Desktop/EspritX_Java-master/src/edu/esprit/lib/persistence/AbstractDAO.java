package edu.esprit.lib.persistence;

import edu.esprit.lib.persistence.annotation.HasMany;
import edu.esprit.lib.persistence.fx.PersistentListProperty;
import edu.esprit.lib.persistence.sqlbuilder.DatabaseType;
import edu.esprit.lib.persistence.sqlbuilder.QueryBuilder;
import edu.esprit.lib.persistence.sqlbuilder.TableUtils;
import edu.esprit.lib.persistence.sqlbuilder.mapper.CountRowMapper;
import edu.esprit.lib.persistence.sqlbuilder.mapper.RowToEntityMapper;
import edu.esprit.lib.persistence.sqlbuilder.mapper.RowToScalarMapper;
import edu.esprit.lib.persistence.sqlbuilder.query.Delete;
import edu.esprit.lib.persistence.sqlbuilder.query.Insert;
import edu.esprit.lib.persistence.sqlbuilder.query.Update;
import edu.esprit.lib.persistence.sqlbuilder.query.select.OrderByType;
import edu.esprit.utils.DataSource;
import javafx.beans.property.Property;
import org.apache.commons.collections.CollectionUtils;
import org.pacesys.reflect.Reflect;

import javax.management.ReflectionException;
import javax.persistence.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


public abstract class AbstractDAO<T extends IdentifiableEntity> {

    protected Class<? extends IdentifiableEntity> entityClass;
    protected List<Field> persistentFields;
    protected List<Field> hasManyFields;
    protected List<String> columns;
    protected String tableName;

    public AbstractDAO() {
        Type genericSuperClass = getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperClass;
        Type typeArgument = parameterizedType.getActualTypeArguments()[0];
        entityClass = (Class<? extends IdentifiableEntity>) typeArgument;
        Table table = entityClass.getAnnotation(Table.class);
        if (table != null) {
            tableName = table.name();
        } else {
            tableName = TableUtils.getTableName(entityClass);
        }
        this.persistentFields = new ArrayList<>();
        this.hasManyFields = new ArrayList<>();
        this.columns = new ArrayList<>();
        this.tableName = TableUtils.getTableName(entityClass);
        Field[] fields = entityClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Transient.class))
                continue;
            if (field.isAnnotationPresent(Column.class) && !field.getName().equals("serialVersionUID")) {
                columns.add(TableUtils.getColumnName(field));
                persistentFields.add(field);
            }
            if (field.isAnnotationPresent(HasMany.class)) {
                hasManyFields.add(field);
            }
        }
    }

    protected QueryBuilder getQueryBuilder() {
        return new QueryBuilder(DatabaseType.MYSQL, DataSource.getConnection());
    }

    public int count() throws SQLException {
        return getQueryBuilder()
                .select()
                .count("*")
                .from()
                .table(tableName)
                .single(new CountRowMapper());
    }

    public T findSingle(String column, Object value) throws SQLException {
        return this.getQueryBuilder()
                .select()
                .all()
                .from()
                .table(tableName)
                .where(String.format("`%s` = %s", column, value))
                .single(new RowToEntityMapper<T>(entityClass));
    }

    public int findLastId() {
        int id = -1;
        try {
            id = this.getQueryBuilder()
                    .select()
                    .column("id")
                    .from()
                    .table(tableName)
                    .orderBy()
                    .column("id", OrderByType.DESC)
                    .limit(0, 1)
                    .single(new RowToScalarMapper<>(Integer.class));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return id;
    }

    public T findById(Integer id) {
        List<T> retVal = findBy("id = " + id);
        if (retVal.size() > 0) {
            return retVal.get(0);
        } else {
            return null;
        }
    }

    public List<T> findAll() {
        List<T> query = null;
        try {
            query = this.getQueryBuilder()
                    .select()
                    .all()
                    .from()
                    .table(tableName)
                    .list(new RowToEntityMapper<T>(entityClass));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return query;
    }

    public List<T> findBy(String criteria) {
        List<T> query = null;
        try {
            query = this.getQueryBuilder()
                    .select()
                    .all()
                    .from()
                    .table(tableName)
                    .where(criteria)
                    .list(new RowToEntityMapper<T>(entityClass));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return query;
    }

    public int insert(T entity) {
        List<Method> prePersistCallbacks = Reflect.on(entityClass).methods().annotatedWithRecursive(PrePersist.class);
        for (Method method : prePersistCallbacks) {
            try {
                Reflect.on(method).against(entity).call();
            } catch (ReflectionException e) {
                e.printStackTrace();
            }
        }

        Object[] valuesArray = getValues(entity)
                .stream()
                .map(x -> PropertyConverter.convertToPrimitive((Property) x)).toArray();

        String qb = this.getQueryBuilder()
                .insert(tableName)
                .columns(this.columns.toArray(new String[0]))
                .values(valuesArray)
                .toString();
        int affectedRows = DataSource.executeUpdate(qb);
        entity.setId(findLastId()); // this can cause a race condition.. Fix me later. Maybe with transactions?
        entity.setPersisted(true);

        for (Field field : hasManyFields) {
            try {
                HasMany relation = field.getAnnotation(HasMany.class);
                Insert query = getQueryBuilder()
                        .insert(relation.JoinTable())
                        .columns(relation.LocalColumn(), relation.ForeignColumn());
                for (IdentifiableEntity value : (List<? extends IdentifiableEntity>) field.get(entity)) {
                    query.values(entity.getId(), value.getId());
                }
                DataSource.executeUpdate(query.toString());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return affectedRows;
    }

    public int update(T entity) {
        List<Method> prePersistCallbacks = Reflect.on(entityClass).methods().annotatedWithRecursive(PreUpdate.class);
        for (Method method : prePersistCallbacks) {
            try {
                Reflect.on(method).against(entity).call();
            } catch (ReflectionException e) {
                e.printStackTrace();
            }
        }

        Update query = this.getQueryBuilder()
                .update(tableName);
        for (int i = 0; i < persistentFields.size(); i++) {
            try {
                query.set(columns.get(i), PropertyConverter.convertToPrimitive((Property) persistentFields.get(i).get(entity)));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        query.where("id = " + entity.getId());
        int affectedRows = DataSource.executeUpdate(query.toString());
        entity.setPersisted(true);
        for (Field field : hasManyFields) {
            try {
                HasMany annotation = field.getAnnotation(HasMany.class);
                List<Integer> memoryIds = ((PersistentListProperty) field.get(entity))
                        .get()
                        .stream()
                        .mapToInt(x -> ((IdentifiableEntity) x).getId())
                        .boxed().toList();

                List<Integer> dbIds = getQueryBuilder()
                        .select()
                        .columns(annotation.ForeignColumn())
                        .from()
                        .table(annotation.JoinTable())
                        .where(String.format("`%s`=%s", annotation.LocalColumn(), entity.getId()))
                        .list(new RowToScalarMapper<>(Integer.class));

                // check if newsIds and dbIds have the same elements
                if (CollectionUtils.isEqualCollection(memoryIds, dbIds))
                    continue;

                // get items in memoryIds that are not in dbIds
                List<Integer> toAdd = memoryIds.stream()
                        .filter(x -> !dbIds.contains(x))
                        .collect(Collectors.toList());

                // get items in dbIds that are not in memoryIds
                List<Integer> toRemove = dbIds.stream()
                        .filter(x -> !memoryIds.contains(x))
                        .collect(Collectors.toList());

                if (toAdd.size() > 0) {
                    Insert query2 = getQueryBuilder()
                            .insert(annotation.JoinTable())
                            .columns(annotation.LocalColumn(), annotation.ForeignColumn());
                    toAdd.forEach(x -> query2.values(entity.getId(), x));
                    DataSource.executeUpdate(query2.toString());
                }

                if (toRemove.size() > 0) {
                    Delete query2 = getQueryBuilder()
                            .delete(String.format(annotation.JoinTable()))
                            .where(annotation.LocalColumn() + " = " + entity.getId())
                            .and(String.format("`%s` IN (%s)", annotation.ForeignColumn(),
                                    toRemove.stream()
                                            .map(Object::toString)
                                            .collect(Collectors.joining(","))));
                    DataSource.executeUpdate(query2.toString());
                }
            } catch (IllegalAccessException | SQLException e) {
                e.printStackTrace();
            }
        }
        //entity.setId(findLastId()); // this can cause a race condition.. Fix me later. Maybe with transactions?
        return affectedRows;
    }

    public int delete(T entity) throws IllegalArgumentException {
        for (Field field : hasManyFields) {
            HasMany relation = field.getAnnotation(HasMany.class);
            String query = this.getQueryBuilder()
                    .delete(relation.JoinTable())
                    .where(String.format("`%s` = %s", relation.LocalColumn(), entity.getId()))
                    .toString();
            int affectedRows = DataSource.executeUpdate(query);
        }
        String query = this.getQueryBuilder().delete(tableName).where("id = " + entity.getId()).toString();
        int affectedRows = DataSource.executeUpdate(query);
        entity.setPersisted(false);
        return affectedRows;
    }

    public int persist(T entity) throws IllegalArgumentException {
        if (entity != null) {
            if (!entity.getPersisted()) {
                return this.insert(entity);
            } else {
                return this.update(entity);
            }
        } else {
            throw new NullPointerException("Trying to save null entity. Go drunk, you're home.");
        }
    }

    protected List<Object> getValues(T object) {
        List<Object> values = new ArrayList<>();
        for (Field field : persistentFields) {
            try {
                Object value = field.get(object);
                if (value instanceof String || value instanceof Date) {
                    values.add("'" + field.get(object).toString() + "'");
                } else {
                    values.add(field.get(object));
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return values;
    }

}

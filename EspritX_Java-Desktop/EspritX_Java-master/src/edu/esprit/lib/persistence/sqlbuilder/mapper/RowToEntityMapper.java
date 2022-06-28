package edu.esprit.lib.persistence.sqlbuilder.mapper;

import edu.esprit.lib.persistence.annotation.HasMany;
import edu.esprit.lib.persistence.IdentifiableEntity;
import edu.esprit.utils.DataSource;
import edu.esprit.lib.persistence.PropertyConverter;
import edu.esprit.lib.persistence.fx.PersistentListProperty;
import edu.esprit.lib.persistence.sqlbuilder.DatabaseType;
import edu.esprit.lib.persistence.sqlbuilder.QueryBuilder;
import edu.esprit.lib.persistence.sqlbuilder.TableUtils;
import edu.esprit.lib.persistence.sqlbuilder.query.select.RowMapper;
import org.pacesys.reflect.Reflect;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Supplier;

public class RowToEntityMapper<T extends IdentifiableEntity> extends RowMapper<T> {
    private final List<Field> persistentFields;
    private final List<Field> hasManyFields;
    protected Class<?> entityClass;

    public RowToEntityMapper(Class<? extends IdentifiableEntity> entityClass) {
        this.entityClass = entityClass;
        this.persistentFields = Reflect.on(entityClass).fields().annotatedWith(Column.class);
        this.hasManyFields = Reflect.on(entityClass).fields().annotatedWith(HasMany.class);
    }

    private QueryBuilder getQueryBuilder() throws SQLException {
        return new QueryBuilder(DatabaseType.MYSQL, DataSource.getConnection());
    }

    @Override
    public T convert(ResultSet resultSet, int rowNum) {
        try {
            T object = (T) entityClass.newInstance();
            Object id = resultSet.getObject("id");
            object.setId((int) id);
            object.setPersisted(true);
            for (Field persistentField : persistentFields) {
                String columnLabel = TableUtils.getColumnName(persistentField);
                Object resultField = resultSet.getObject(columnLabel);
                if (resultField != null) {
                    persistentField.set(object, persistentField.getType().cast(PropertyConverter.convertToProperty(resultField, persistentField)));
                } else {
                    persistentField.set(object, persistentField.getType().newInstance());
                }
            }
            for (Field hasManyField : hasManyFields) {
                HasMany relation = hasManyField.getAnnotation(HasMany.class);

                Supplier<List<IdentifiableEntity>> listSupplier = () -> {
                    try {
                        return getQueryBuilder()
                                .select()
                                .column("t2.*")
                                .from()
                                .table(TableUtils.getTableName(entityClass) + " t1")
                                .table(TableUtils.getTableName(relation.TargetEntity()) + " t2")
                                .join(String.format("%s jt ON t2.id = jt.%s", relation.JoinTable(), relation.ForeignColumn()))
                                .where(String.format("t1.id = jt.%s", relation.LocalColumn()))
                                .and(String.format("jt.%s = t2.id", relation.ForeignColumn()))
                                .and(String.format("t1.id = %s", id))
                                .list(new RowToEntityMapper<>(relation.TargetEntity()));
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                        return null;
                    }
                };

                hasManyField.set(object, new PersistentListProperty<>(listSupplier));
            }
            return object;
        } catch (InstantiationException | IllegalAccessException | SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

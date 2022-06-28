package edu.esprit.lib.fx.converter;

import edu.esprit.lib.persistence.AbstractDAO;
import edu.esprit.lib.persistence.IdentifiableEntity;
import edu.esprit.lib.persistence.DAOLocator;
import javafx.beans.property.Property;
import javafx.util.StringConverter;
import org.pacesys.reflect.Reflect;

import javax.persistence.Column;
import java.lang.reflect.Field;
import java.sql.SQLException;

public class EntityConverter<E extends IdentifiableEntity> extends StringConverter<E> {
    private Class<E> clazz;
    private Field field;

    public EntityConverter(Class<E> clazz) {
        this.clazz = clazz;
        this.field = Reflect.on(clazz).fields().named("id");
    }

    public EntityConverter(Field field) {
        this.clazz = (Class<E>) field.getDeclaringClass();
        this.field = field;
    }

    @Override
    public String toString(E object) {
        try {
            return ((Property) field.get(object)).getValue().toString();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public E fromString(String string) {
        AbstractDAO<E> dao = DAOLocator.GetDAO(clazz);
        try {
            return dao.findSingle(this.field.getAnnotation(Column.class).name(), string);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}

package edu.esprit.lib.persistence;

import edu.esprit.lib.persistence.annotation.Enumeration;
import edu.esprit.lib.persistence.fx.PersistentListProperty;
import edu.esprit.utils.EnumUtils;
import javafx.beans.property.*;
import javafx.collections.FXCollections;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.function.Supplier;

public class PropertyConverter {

    public static Property convertToProperty(Object value, Field field) {
        if (field.getAnnotation(Enumeration.class) != null) {
            {
                try {
                    Class<? extends QueryableEnum<String>> clazz = (Class<? extends QueryableEnum<String>>) field.getAnnotation(Enumeration.class).type();
                    Object val = EnumUtils.getEnum(clazz, (String) value);
                    return new SimpleObjectProperty<>(val);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return convertToProperty(value);
    }

    public static Property convertToProperty(Object primitive) {
        if (primitive instanceof Boolean) {
            return new SimpleBooleanProperty((Boolean) primitive);
        }
        if (primitive instanceof Integer) {
            return new SimpleIntegerProperty((Integer) primitive);
        }
        if (primitive instanceof String) {
            return new SimpleStringProperty((String) primitive);
        }
        if (primitive instanceof Double) {
            return new SimpleDoubleProperty((Double) primitive);
        }
        if (primitive instanceof Float) {
            return new SimpleFloatProperty((Float) primitive);
        }
        if (primitive instanceof Long) {
            return new SimpleLongProperty((Long) primitive);
        }
        if (primitive instanceof List) {
            return new SimpleListProperty(FXCollections.observableList((List<? extends IdentifiableEntity>) primitive));
        }
        if (primitive instanceof Supplier) {
            return new PersistentListProperty((Supplier<List<? extends IdentifiableEntity>>) primitive);

        }
        return new SimpleObjectProperty<>(primitive);
    }

    public static Object convertToPrimitive(Property property) {
        if (property instanceof ObjectProperty objectProperty) {
            if (property.getValue() instanceof QueryableEnum) {
                return ((QueryableEnum) objectProperty.getValue()).getValue();
            } else {
                return property.getValue();
            }
        }
        if (property.getValue() == null) {
            try {
                Class<?> clazz = (Class<?>) ((ParameterizedType) ((Class<?>) property.getClass().getGenericSuperclass()).getSuperclass().getGenericInterfaces()[0]).getActualTypeArguments()[0];
                return clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                return null;
            }
        }
        if (property instanceof BooleanProperty) {
            return ((BooleanProperty) property).getValue();
        }
        if (property instanceof IntegerProperty) {
            return ((IntegerProperty) property).getValue();
        }
        if (property instanceof StringProperty) {
            return ((StringProperty) property).getValue();
        }
        if (property instanceof DoubleProperty) {
            return ((DoubleProperty) property).getValue();
        }
        if (property instanceof FloatProperty) {
            return ((FloatProperty) property).getValue();
        }
        if (property instanceof LongProperty) {
            return ((LongProperty) property).getValue();
        }
        throw new UnsupportedOperationException("Trying to convert unsupported property type");
    }
}

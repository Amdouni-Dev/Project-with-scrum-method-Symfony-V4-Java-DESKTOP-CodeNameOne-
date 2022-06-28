package edu.esprit.lib.persistence.sqlbuilder;


import javax.persistence.Column;
import javax.persistence.Table;
import java.lang.reflect.Field;

public class TableUtils {

    public static String getTableName(String className) {
        className = className == null ? "" : className.trim();
        String tableName = "";
        if (!className.isEmpty()) {
            tableName = inflect(className);
        }
        return tableName;
    }

    private static String inflect(String className) {
        if (className.matches("([A-Z](\\w+))+")) {
            String[] words = className.split("(?=[A-Z])");
            if (words.length >= 2) {
                StringBuilder classNameBuilder = new StringBuilder();
                for (String word : words) {
                    if (!word.isEmpty()) {
                        classNameBuilder.append(word);
                    }
                }
                className = classNameBuilder.toString();
            }
        }
        return className.trim().replaceAll("([a-z0-9]+)([A-Z])", "$1_$2").toLowerCase();
    }

    public static String getTableName(Class<?> c) {
        String tableName = "";
        if (c != null) {
            String className = c.getSimpleName();
            Table tableAnnotation = c.getAnnotation(Table.class);
            if (tableAnnotation != null && !tableAnnotation.name().trim().isEmpty()) {
                className = tableAnnotation.name().toLowerCase();
            }
            tableName = inflect(className);
        }
        return tableName;
    }

    public static String getColumnName(String s) {
        String columnName = "";
        if (s != null && !s.isEmpty()) {
            columnName = s;
            columnName = columnName.trim();
            columnName = columnName.replaceAll("([a-z0-9]+)([A-Z])", "$1_$2");
            columnName = columnName.toLowerCase();
        }
        return columnName;
    }

    public static String getColumnName(Field f) {
        String columnName = "";
        if (f != null) {
            if (f.getAnnotation(Column.class) != null) {
                return f.getAnnotation(Column.class).name().trim();
            }
            columnName = getColumnName(f.getName());
        }
        return columnName;
    }

    public static String getColumnName(Class<?> c) {
        String columnName = "";
        if (c != null) {
            columnName = getColumnName(c.getSimpleName());
        }
        return columnName;
    }

    public static String getFieldName(String columnName) {
        StringBuilder fieldName = new StringBuilder();
        if (columnName != null && !columnName.trim().isEmpty()) {
            fieldName = new StringBuilder(columnName.trim().replace("_id", ""));
            if (fieldName.toString().matches("(\\w+_\\w+)+")) {
                String[] words = fieldName.toString().split("_");
                if (words.length >= 2) {
                    fieldName = new StringBuilder();
                    for (String word : words) {
                        if (!word.isEmpty()) {
                            fieldName.append(String.format("%s%s", word.substring(0, 1).toUpperCase(), word.substring(1)));
                        }
                    }
                }
            }
        }
        return fieldName.toString();
    }

    public static String getTableName(Class<?> c1, Class<?> c2) {
        return String.format("%s_%s", getTableName(c1), getTableName(c2));
    }
}

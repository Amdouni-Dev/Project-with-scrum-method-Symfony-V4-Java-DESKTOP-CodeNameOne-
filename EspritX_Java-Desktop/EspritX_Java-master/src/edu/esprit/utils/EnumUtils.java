package edu.esprit.utils;

import edu.esprit.lib.persistence.QueryableEnum;

public class EnumUtils {
    public static <T extends QueryableEnum<String>> T getEnum(Class<T> enumType, String value) {
        if (value == null) {
            return null;
        }
        for (T t : enumType.getEnumConstants()) {
            if (t.getValue().equalsIgnoreCase(value)) {
                return t;
            }
        }
        return null;
    }
}

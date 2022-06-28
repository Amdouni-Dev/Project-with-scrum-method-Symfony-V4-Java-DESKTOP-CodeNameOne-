package edu.esprit.enums;

import edu.esprit.lib.persistence.QueryableEnum;

public enum GroupType implements QueryableEnum<String> {
    SUPER_ADMIN("super admin"),
    STUDENT("student"),
    SITE_STAFF("site staff"),
    FACULTY_STAFF("faculty staff"),
    TEACHERS("teachers");

    private final String value;

    GroupType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

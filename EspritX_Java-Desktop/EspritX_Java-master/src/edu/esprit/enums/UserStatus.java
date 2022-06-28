package edu.esprit.enums;

import edu.esprit.lib.persistence.QueryableEnum;

public enum UserStatus implements QueryableEnum<String> {
    ACTIVE("active"),
    PENDING("pending"),
    RESTRICTED("restricted"),
    ALUMNUS("alumnus");

    private final String value;

    UserStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

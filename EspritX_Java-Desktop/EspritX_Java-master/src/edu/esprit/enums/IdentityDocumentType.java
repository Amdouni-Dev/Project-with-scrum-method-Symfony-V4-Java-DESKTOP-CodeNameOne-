package edu.esprit.enums;

import edu.esprit.lib.persistence.QueryableEnum;

public enum IdentityDocumentType implements QueryableEnum<String> {
    CIN("cin"), PASSPORT("passport"), UNKNOWN("Unknown");

    private final String value;

    IdentityDocumentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

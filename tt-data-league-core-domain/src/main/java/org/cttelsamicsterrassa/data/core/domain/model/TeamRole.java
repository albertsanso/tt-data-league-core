package org.cttelsamicsterrassa.data.core.domain.model;

public enum TeamRole {
    LOCAL("local"),
    VISITOR("visitor"),
    UNKNOWN("unknown");

    private final String value;

    TeamRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * Builder method to map string values to TeamRole enum.
     * @param value the string value to map
     * @return the corresponding TeamRole enum, or UNKNOWN if not found
     */
    public static TeamRole fromValue(String value) {
        if (value == null || value.isEmpty()) {
            return UNKNOWN;
        }

        for (TeamRole role : TeamRole.values()) {
            if (role.value.equalsIgnoreCase(value)) {
                return role;
            }
        }

        return UNKNOWN;
    }
}

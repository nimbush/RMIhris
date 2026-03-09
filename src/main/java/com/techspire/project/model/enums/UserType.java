package com.techspire.project.model.enums;

import java.io.Serializable;

public enum UserType implements Serializable {

    HR,
    EMPLOYEE;

    public static UserType fromString(String value) {
        for (UserType type : UserType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid user type: " + value);
    }
}
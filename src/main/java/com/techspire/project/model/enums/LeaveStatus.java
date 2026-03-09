package com.techspire.project.model.enums;

import java.io.Serializable;

public enum LeaveStatus implements Serializable {
    PENDING,
    APPROVED,
    REJECTED;

    public static LeaveStatus fromString(String value) {
        for (LeaveStatus status : values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid leave status: " + value);
    }
}
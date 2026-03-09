package com.techspire.project.dto;

import com.techspire.project.model.enums.UserType;

import java.io.Serializable;

public class LoginResponseDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private boolean success;
    private String message;
    private String icNumber;
    private String username;
    private UserType userType;
    private boolean defaultPassword;

    public LoginResponseDto() {
    }

    public LoginResponseDto(boolean success, String message, String icNumber, String username, UserType userType, boolean defaultPassword) {
        this.success = success;
        this.message = message;
        this.icNumber = icNumber;
        this.username = username;
        this.userType = userType;
        this.defaultPassword = defaultPassword;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIcNumber() {
        return icNumber;
    }

    public void setIcNumber(String icNumber) {
        this.icNumber = icNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public boolean isDefaultPassword() {
        return defaultPassword;
    }

    public void setDefaultPassword(boolean defaultPassword) {
        this.defaultPassword = defaultPassword;
    }
}
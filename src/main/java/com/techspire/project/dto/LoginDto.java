package com.techspire.project.dto;

import java.io.Serializable;

public class LoginDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String icNumber;
    private String username;
    private String password;

    public LoginDto() {
    }

    public LoginDto(String icNumber, String username, String password) {
        this.icNumber = icNumber;
        this.username = username;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
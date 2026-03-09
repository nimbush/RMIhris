package com.techspire.project.dto;

import java.io.Serializable;

public class CredentialDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String username;
    private String password;
    private String icNumber;

    public CredentialDto() {
    }

    public CredentialDto(String username, String password, String icNumber) {
        this.username = username;
        this.password = password;
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

    public String getIcNumber() {
        return icNumber;
    }

    public void setIcNumber(String icNumber) {
        this.icNumber = icNumber;
    }
}
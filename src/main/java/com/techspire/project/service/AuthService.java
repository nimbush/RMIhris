package com.techspire.project.service;

import com.techspire.project.dto.LoginResponseDto;

public interface AuthService {
    LoginResponseDto logIn(String icNumber, String username, String password) throws Exception;
}
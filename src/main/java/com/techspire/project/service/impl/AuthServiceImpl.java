package com.techspire.project.service.impl;

import com.techspire.project.dto.LoginResponseDto;
import com.techspire.project.model.Credential;
import com.techspire.project.model.User;
import com.techspire.project.repository.CredentialRepository;
import com.techspire.project.repository.UserRepository;
import com.techspire.project.repository.impl.CredentialRepositoryImpl;
import com.techspire.project.repository.impl.UserRepositoryImpl;
import com.techspire.project.service.AuthService;
import com.techspire.project.util.PasswordUtil;

public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final CredentialRepository credentialRepository;

    public AuthServiceImpl() {
        this.userRepository = new UserRepositoryImpl();
        this.credentialRepository = new CredentialRepositoryImpl();
    }

    @Override
    public LoginResponseDto logIn(String icNumber, String username, String password) throws Exception {
        if (icNumber == null || icNumber.trim().isEmpty()) {
            return new LoginResponseDto(false, "IC number is required", null, null, null, false);
        }
        if (username == null || username.trim().isEmpty()) {
            return new LoginResponseDto(false, "Username is required", null, null, null, false);
        }
        if (password == null || password.trim().isEmpty()) {
            return new LoginResponseDto(false, "Password is required", null, null, null, false);
        }

        User user = userRepository.findByIcNumber(icNumber);
        if (user == null) {
            return new LoginResponseDto(false, "INVALID_USER", null, null, null, false);
        }

        Credential credential = credentialRepository.findByUserId(user.getId());
        if (credential == null) {
            return new LoginResponseDto(false, "CREDENTIAL_NOT_FOUND", null, null, null, false);
        }

        if (!credential.getUsername().equals(username)) {
            return new LoginResponseDto(false, "INVALID_USERNAME", null, null, null, false);
        }

        if (!PasswordUtil.verifyPassword(password, credential.getPasswordHash())) {
            return new LoginResponseDto(false, "INVALID_PASSWORD", null, null, null, false);
        }

        return new LoginResponseDto(
                true,
                "LOGIN_SUCCESS",
                user.getIcNumber(),
                credential.getUsername(),
                user.getUserType(),
                credential.isDefaultPassword()
        );
    }
}
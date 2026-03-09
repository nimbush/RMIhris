package com.techspire.project.repository;

import com.techspire.project.model.Credential;

public interface CredentialRepository {

    boolean save(Credential credential) throws Exception;

    Credential findByUserId(Long userId) throws Exception;

    boolean existsByUserId(Long userId) throws Exception;

    boolean existsByUsername(String username) throws Exception;

    boolean updatePasswordByUserId(Long userId, String passwordHash, boolean defaultPassword) throws Exception;
}
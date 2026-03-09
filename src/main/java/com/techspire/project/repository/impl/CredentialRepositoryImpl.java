package com.techspire.project.repository.impl;

import com.techspire.project.config.DatabaseConfig;
import com.techspire.project.model.Credential;
import com.techspire.project.repository.CredentialRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CredentialRepositoryImpl implements CredentialRepository {

    @Override
    public boolean save(Credential credential) throws Exception {
        String sql = "INSERT INTO credentials (userId, username, passwordHash, defaultPassword) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, credential.getUserId());
            ps.setString(2, credential.getUsername());
            ps.setString(3, credential.getPasswordHash());
            ps.setBoolean(4, credential.isDefaultPassword());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public Credential findByUserId(Long userId) throws Exception {
        String sql = "SELECT * FROM credentials WHERE userId = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Credential credential = new Credential();
                    credential.setId(rs.getLong("id"));
                    credential.setUserId(rs.getLong("userId"));
                    credential.setUsername(rs.getString("username"));
                    credential.setPasswordHash(rs.getString("passwordHash"));
                    credential.setDefaultPassword(rs.getBoolean("defaultPassword"));
                    return credential;
                }
            }
        }

        return null;
    }

    @Override
    public boolean existsByUserId(Long userId) throws Exception {
        String sql = "SELECT 1 FROM credentials WHERE userId = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public boolean existsByUsername(String username) throws Exception {
        String sql = "SELECT 1 FROM credentials WHERE username = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public boolean updatePasswordByUserId(Long userId, String passwordHash, boolean defaultPassword) throws Exception {
        String sql = "UPDATE credentials SET passwordHash = ?, defaultPassword = ? WHERE userId = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, passwordHash);
            ps.setBoolean(2, defaultPassword);
            ps.setLong(3, userId);

            return ps.executeUpdate() == 1;
        }
    }
}
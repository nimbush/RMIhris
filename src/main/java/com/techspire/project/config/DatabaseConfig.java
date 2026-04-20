package com.techspire.project.config;

import com.techspire.project.util.PasswordUtil;

import java.sql.*;

public class DatabaseConfig {

    private static final String ROOT_URL = "jdbc:mysql://localhost:3307/?useSSL=false&serverTimezone=UTC";
    private static final String DB_URL = "jdbc:mysql://localhost:3307/newDatabase?useSSL=false&serverTimezone=UTC";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL Driver not found", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
    }

    public static void initializeDatabase() throws SQLException {
        try (Connection connection = DriverManager.getConnection(ROOT_URL, USERNAME, PASSWORD);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS newDatabase");
        }

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS users (" +
                            "id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "firstName VARCHAR(100) NOT NULL, " +
                            "lastName VARCHAR(100) NOT NULL, " +
                            "icNumber VARCHAR(100) NOT NULL UNIQUE, " +
                            "userType VARCHAR(50) NOT NULL, " +
                            "fatherName VARCHAR(100), " +
                            "motherName VARCHAR(100), " +
                            "phoneNumber VARCHAR(20), " +
                            "address VARCHAR(255), " +
                            "leaveCount INT DEFAULT 0" +
                            ")"
            );

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS credentials (" +
                            "id BIGINT PRIMARY KEY AUTO_INCREMENT, " +
                            "userId BIGINT NOT NULL UNIQUE, " +
                            "username VARCHAR(50) NOT NULL UNIQUE, " +
                            "passwordHash VARCHAR(255) NOT NULL, " +
                            "defaultPassword BOOLEAN DEFAULT TRUE, " +
                            "FOREIGN KEY (userId) REFERENCES users(id) ON DELETE CASCADE" +
                            ")"
            );

            statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS leave_records (\n" +
                            "    id BIGINT AUTO_INCREMENT PRIMARY KEY,\n" +
                            "    user_id BIGINT NOT NULL,\n" +
                            "    leave_days INT NOT NULL,\n" +
                            "    status ENUM('PENDING','APPROVED','REJECTED') DEFAULT 'PENDING',\n" +
                            "    applied_date DATE NOT NULL,\n" +
                            "    from_date DATE NOT NULL,\n" +
                            "    to_date DATE NOT NULL,\n" +
                            "    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE\n" +
                            ");"
            );
        }

//        createDefaultHrAdmin();
    }

    private static void createDefaultHrAdmin() throws SQLException {
        String checkUserSql = "SELECT id FROM users WHERE icNumber = ?";
        String insertUserSql = "INSERT INTO users (firstName, lastName, icNumber, userType, leaveCount) VALUES (?, ?, ?, ?, ?)";
        String insertCredentialSql = "INSERT INTO credentials (userId, username, passwordHash, defaultPassword) VALUES (?, ?, ?, ?)";

        try (Connection connection = getConnection()) {
            Long userId = null;

            try (PreparedStatement ps = connection.prepareStatement(checkUserSql)) {
                ps.setString(1, "HR001");
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        userId = rs.getLong("id");
                    }
                }
            }

            if (userId == null) {
                try (PreparedStatement ps = connection.prepareStatement(insertUserSql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, "HR");
                    ps.setString(2, "USER");
                    ps.setString(3, "HR123");
                    ps.setString(4, "HR");
                    ps.setInt(5, 30);
                    ps.executeUpdate();

                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (keys.next()) {
                            userId = keys.getLong(1);
                        }
                    }
                }

                if (userId != null) {
                    try (PreparedStatement ps = connection.prepareStatement(insertCredentialSql)) {
                        ps.setLong(1, userId);
                        ps.setString(2, "hr");
                        ps.setString(3, PasswordUtil.hashPassword("Test@123"));
                        ps.setBoolean(4, false);
                        ps.executeUpdate();
                    }
                }
            }
        }
    }
}
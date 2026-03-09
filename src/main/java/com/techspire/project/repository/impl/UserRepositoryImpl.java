package com.techspire.project.repository.impl;

import com.techspire.project.config.DatabaseConfig;
import com.techspire.project.dto.UpdateUserDto;
import com.techspire.project.dto.UserReportDto;
import com.techspire.project.model.User;
import com.techspire.project.model.enums.UserType;
import com.techspire.project.repository.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {

    @Override
    public boolean save(User user) throws Exception {
        String sql = "INSERT INTO users (firstName, lastName, icNumber, userType, fatherName, motherName, phoneNumber, address, leaveCount) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getIcNumber());
            ps.setString(4, user.getUserType().name());
            ps.setString(5, user.getFatherName());
            ps.setString(6, user.getMotherName());
            ps.setString(7, user.getPhoneNumber());
            ps.setString(8, user.getAddress());
            ps.setInt(9, user.getLeaveCount());

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean updateDetails(UpdateUserDto dto) throws Exception {
        String sql = "UPDATE users SET fatherName = ?, motherName = ?, phoneNumber = ?, address = ? WHERE icNumber = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, dto.getFatherName());
            ps.setString(2, dto.getMotherName());
            ps.setString(3, dto.getPhoneNumber());
            ps.setString(4, dto.getAddress());
            ps.setString(5, dto.getIcNumber());

            return ps.executeUpdate() == 1;
        }
    }

    @Override
    public User findById(Long id) throws Exception {
        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToUser(rs);
                }
            }
        }
        return null;
    }

    @Override
    public User findByIcNumber(String icNumber) throws Exception {
        String sql = "SELECT * FROM users WHERE icNumber = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, icNumber);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToUser(rs);
                }
            }
        }
        return null;
    }

    @Override
    public List<User> findAll() throws Exception {
        String sql = "SELECT * FROM users";
        List<User> users = new ArrayList<>();

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                users.add(mapRowToUser(rs));
            }
        }

        return users;
    }

    @Override
    public boolean updateLeaveCount(Long userId, int leaveCount) throws Exception {
        String sql = "UPDATE users SET leaveCount = ? WHERE id = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, leaveCount);
            ps.setLong(2, userId);

            return ps.executeUpdate() == 1;
        }
    }

    @Override
    public boolean deleteByIcNumber(String icNumber) throws Exception {
        String sql = "DELETE FROM users WHERE icNumber = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, icNumber);
            return ps.executeUpdate() == 1;
        }
    }

    @Override
    public List<UserReportDto> getYearlyReport() throws Exception {
        String sql =
                "SELECT u.id, u.firstName, u.lastName, u.userType, u.leaveCount, " +
                        "COALESCE(SUM(CASE WHEN lr.status = 'APPROVED' THEN lr.leaveDays ELSE 0 END), 0) AS approvedLeaveTaken " +
                        "FROM users u " +
                        "LEFT JOIN leave_records lr ON u.id = lr.userId " +
                        "GROUP BY u.id, u.firstName, u.lastName, u.userType, u.leaveCount";

        List<UserReportDto> reports = new ArrayList<>();

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int remainingLeave = rs.getInt("leaveCount");
                int approvedLeaveTaken = rs.getInt("approvedLeaveTaken");

                UserReportDto dto = new UserReportDto();
                dto.setUserId(rs.getLong("id"));
                dto.setFullName(rs.getString("firstName") + " " + rs.getString("lastName"));
                dto.setUserType(rs.getString("userType"));
                dto.setRemainingLeave(remainingLeave);
                dto.setTotalLeave(remainingLeave + approvedLeaveTaken);

                reports.add(dto);
            }
        }

        return reports;
    }

    private User mapRowToUser(ResultSet rs) throws Exception {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setFirstName(rs.getString("firstName"));
        user.setLastName(rs.getString("lastName"));
        user.setIcNumber(rs.getString("icNumber"));
        user.setUserType(UserType.fromString(rs.getString("userType")));
        user.setFatherName(rs.getString("fatherName"));
        user.setMotherName(rs.getString("motherName"));
        user.setPhoneNumber(rs.getString("phoneNumber"));
        user.setAddress(rs.getString("address"));
        user.setLeaveCount(rs.getInt("leaveCount"));
        return user;
    }
}
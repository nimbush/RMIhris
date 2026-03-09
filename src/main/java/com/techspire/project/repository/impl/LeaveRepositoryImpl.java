package com.techspire.project.repository.impl;

import com.techspire.project.config.DatabaseConfig;
import com.techspire.project.model.LeaveRecord;
import com.techspire.project.model.enums.LeaveStatus;
import com.techspire.project.repository.LeaveRepository;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class LeaveRepositoryImpl implements LeaveRepository {

    @Override
    public boolean save(LeaveRecord leaveRecord) throws Exception {
        String sql = "INSERT INTO leave_records (userId, leaveDays, status, appliedDate) VALUES (?, ?, ?, ?)";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, leaveRecord.getUserId());
            ps.setInt(2, leaveRecord.getLeaveDays());
            ps.setString(3, leaveRecord.getStatus().name());
            ps.setDate(4, Date.valueOf(leaveRecord.getAppliedDate()));

            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public List<LeaveRecord> findAll() throws Exception {
        String sql = "SELECT * FROM leave_records";
        List<LeaveRecord> records = new ArrayList<>();

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                records.add(mapRowToLeaveRecord(rs));
            }
        }

        return records;
    }

    @Override
    public List<LeaveRecord> findByIcNumber(String icNumber) throws Exception {

        String sql =
                "SELECT lr.id, lr.userId, lr.leaveDays, lr.status, lr.appliedDate " +
                        "FROM leave_records lr " +
                        "JOIN users u ON lr.userId = u.id " +
                        "WHERE u.icNumber = ?";

        List<LeaveRecord> records = new ArrayList<>();

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, icNumber);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                LeaveRecord record = new LeaveRecord();

                record.setId(rs.getLong("id"));
                record.setUserId(rs.getLong("userId"));
                record.setLeaveDays(rs.getInt("leaveDays"));
                record.setStatus(LeaveStatus.valueOf(rs.getString("status")));
                record.setAppliedDate(rs.getDate("appliedDate").toLocalDate());

                records.add(record);
            }
        }

        return records;
    }

    @Override
    public LeaveRecord findById(Long id) throws Exception {
        String sql = "SELECT * FROM leave_records WHERE id = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRowToLeaveRecord(rs);
                }
            }
        }

        return null;
    }

    @Override
    public boolean updateLeaveStatus(Long leaveRecordId, String status) throws Exception {
        String sql = "UPDATE leave_records SET status = ? WHERE id = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setLong(2, leaveRecordId);

            return ps.executeUpdate() == 1;
        }
    }

    private LeaveRecord mapRowToLeaveRecord(ResultSet rs) throws Exception {
        LeaveRecord record = new LeaveRecord();
        record.setId(rs.getLong("id"));
        record.setUserId(rs.getLong("userId"));
        record.setLeaveDays(rs.getInt("leaveDays"));
        record.setStatus(LeaveStatus.fromString(rs.getString("status")));
        record.setAppliedDate(rs.getDate("appliedDate").toLocalDate());
        return record;
    }

}
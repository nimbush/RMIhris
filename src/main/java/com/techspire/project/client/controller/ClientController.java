package com.techspire.project.client.controller;

import com.techspire.project.dto.CreateUserDto;
import com.techspire.project.dto.LeaveApplyDto;
import com.techspire.project.dto.UpdateUserDto;
import com.techspire.project.dto.UserReportDto;
import com.techspire.project.model.LeaveRecord;
import com.techspire.project.model.User;
import com.techspire.project.remote.Jdbc;

import java.util.List;

public class ClientController {

    private final Jdbc jdbc;

    public ClientController(Jdbc jdbc) {
        this.jdbc = jdbc;
    }

    public boolean createUser(CreateUserDto dto) throws Exception {
        return jdbc.createNewUser(dto);
    }

    public boolean updateUser(UpdateUserDto dto) throws Exception {
        return jdbc.updateUserDetails(dto);
    }


    public String applyLeave(LeaveApplyDto dto) throws Exception {
        return jdbc.applyLeave(dto);
    }

    public List<User> getUserDetails() throws Exception {
        return jdbc.checkDetails();
    }

    public List<LeaveRecord> getLeaveStatus() throws Exception {
        return jdbc.checkStatus();
    }

    public List<UserReportDto> getYearlyReport() throws Exception {
        return jdbc.yearlyReport();
    }

    public List<LeaveRecord> getLeaveStatusByIcNumber(String icNumber) throws Exception {
        return jdbc.checkStatusByIcNumber(icNumber);
    }

    public boolean deleteUser(String icNumber) throws Exception {
        return jdbc.deleteUser(icNumber);
    }

    public boolean approveLeave(Long leaveRecordId) throws Exception {
        return jdbc.approveLeave(leaveRecordId);
    }

    public boolean rejectLeave(Long leaveRecordId) throws Exception {
        return jdbc.rejectLeave(leaveRecordId);
    }

    public User getUserByIcNumber(String icNumber) throws Exception {
        return jdbc.getUserByIcNumber(icNumber);
    }
    public boolean addLeaveCount(String icNumber, int extraLeave) throws Exception {
        return jdbc.addLeaveCount(icNumber, extraLeave);
    }
}
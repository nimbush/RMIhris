package com.techspire.project.remote;

import com.techspire.project.dto.CreateUserDto;
import com.techspire.project.dto.LeaveApplyDto;
import com.techspire.project.dto.LoginResponseDto;
import com.techspire.project.dto.UpdateUserDto;
import com.techspire.project.dto.UserReportDto;
import com.techspire.project.model.LeaveRecord;
import com.techspire.project.model.User;
import com.techspire.project.service.AuthService;
import com.techspire.project.service.LeaveService;
import com.techspire.project.service.ReportService;
import com.techspire.project.service.UserService;
import com.techspire.project.service.impl.AuthServiceImpl;
import com.techspire.project.service.impl.LeaveServiceImpl;
import com.techspire.project.service.impl.ReportServiceImpl;
import com.techspire.project.service.impl.UserServiceImpl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class JdbcRemoteImpl extends UnicastRemoteObject implements Jdbc {

    private final UserService userService;
    private final AuthService authService;
    private final LeaveService leaveService;
    private final ReportService reportService;

    public JdbcRemoteImpl() throws RemoteException {
        super();
        this.userService = new UserServiceImpl();
        this.authService = new AuthServiceImpl();
        this.leaveService = new LeaveServiceImpl();
        this.reportService = new ReportServiceImpl();
    }

    @Override
    public LoginResponseDto logIn(String icNumber, String username, String password) throws Exception {
        return authService.logIn(icNumber, username, password);
    }

    @Override
    public boolean createNewUser(CreateUserDto dto) throws Exception {
        return userService.createNewUser(dto);
    }

    @Override
    public boolean updateUserDetails(UpdateUserDto dto) throws Exception {
        return userService.updateUserDetails(dto);
    }

    @Override
    public boolean deleteUser(String icNumber) throws Exception {
        return userService.deleteUser(icNumber);
    }

    @Override
    public boolean changePassword(String icNumber, String newPassword) throws Exception {
        return userService.changePassword(icNumber, newPassword);
    }

    @Override
    public String applyLeave(LeaveApplyDto dto) throws Exception {
        return leaveService.applyLeave(dto);
    }

    @Override
    public boolean approveLeave(Long leaveRecordId) throws Exception {
        return leaveService.approveLeave(leaveRecordId);
    }

    @Override
    public boolean rejectLeave(Long leaveRecordId) throws Exception {
        return leaveService.rejectLeave(leaveRecordId);
    }

    @Override
    public List<LeaveRecord> checkStatus() throws Exception {
        return leaveService.checkStatus();
    }

    @Override
    public List<LeaveRecord> checkStatusByIcNumber(String icNumber) throws RemoteException {

        try {
            return leaveService.getLeaveStatusByIcNumber(icNumber);
        } catch (Exception e) {
            throw new RemoteException("Failed to fetch leave records", e);
        }
    }

    @Override
    public List<User> checkDetails() throws Exception {
        return userService.checkDetails();
    }

    @Override
    public List<UserReportDto> yearlyReport() throws Exception {
        return reportService.yearlyReport();
    }

    @Override
    public User getUserByIcNumber(String icNumber) throws RemoteException {
        try {
            return userService.getUserByIcNumber(icNumber);
        } catch (Exception e) {
            throw new RemoteException("Failed to fetch user", e);
        }
    }

    @Override
    public boolean addLeaveCount(String icNumber, int extraLeave) throws Exception {
        return userService.addLeaveCount(icNumber, extraLeave);
    }
}
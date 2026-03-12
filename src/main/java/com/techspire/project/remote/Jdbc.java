package com.techspire.project.remote;

import com.techspire.project.dto.*;
import com.techspire.project.model.LeaveRecord;
import com.techspire.project.model.User;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Jdbc extends Remote {

    LoginResponseDto logIn(String icNumber, String username, String password) throws RemoteException, Exception;

    boolean createNewUser(CreateUserDto dto) throws RemoteException, Exception;

    boolean updateUserDetails(UpdateUserDto dto) throws RemoteException, Exception;

    boolean deleteUser(String icNumber) throws RemoteException, Exception;

    boolean changePassword(String icNumber, String newPassword) throws RemoteException, Exception;

    String applyLeave(LeaveApplyDto dto) throws RemoteException, Exception;

    boolean approveLeave(Long leaveRecordId) throws RemoteException, Exception;

    boolean rejectLeave(Long leaveRecordId) throws RemoteException, Exception;

    List<LeaveRecord> checkStatus() throws RemoteException, Exception;

    List<LeaveRecord> checkStatusByIcNumber(String icNumber) throws RemoteException, Exception;

    List<User> checkDetails() throws RemoteException, Exception;

    List<UserReportDto> yearlyReport() throws RemoteException, Exception;

    User getUserByIcNumber(String icNumber) throws RemoteException;

    boolean addLeaveCount(String icNumber, int extraLeave) throws Exception;

    UserDetailedReportDto generateUserReport(String icNumber) throws RemoteException, Exception;
}
package com.techspire.project.service;

import com.techspire.project.dto.CreateUserDto;
import com.techspire.project.dto.UpdateUserDto;
import com.techspire.project.model.User;

import java.util.List;

public interface UserService {

    boolean createNewUser(CreateUserDto dto) throws Exception;

    boolean updateUserDetails(UpdateUserDto dto) throws Exception;

    boolean deleteUser(String icNumber) throws Exception;

    boolean changePassword(String icNumber, String newPassword) throws Exception;

    User getUserById(Long id) throws Exception;

    User getUserByIcNumber(String icNumber) throws Exception;

    List<User> checkDetails() throws Exception;

    boolean addLeaveCount(String icNumber, int extraLeave) throws Exception;

}
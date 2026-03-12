package com.techspire.project.repository;

import com.techspire.project.dto.UpdateUserDto;
import com.techspire.project.dto.UserDetailedReportDto;
import com.techspire.project.dto.UserReportDto;
import com.techspire.project.model.User;

import java.util.List;

public interface UserRepository {

    boolean save(User user) throws Exception;

    boolean updateDetails(UpdateUserDto dto) throws Exception;

    User findById(Long id) throws Exception;

    User findByIcNumber(String icNumber) throws Exception;

    List<User> findAll() throws Exception;

    boolean updateLeaveCount(Long userId, int leaveCount) throws Exception;

    boolean deleteByIcNumber(String icNumber) throws Exception;

    List<UserReportDto> getYearlyReport() throws Exception;

    UserDetailedReportDto getDetailedReportByIcNumber(String icNumber) throws Exception;
}
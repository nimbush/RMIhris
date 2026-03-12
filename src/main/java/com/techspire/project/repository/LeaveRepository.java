package com.techspire.project.repository;

import com.techspire.project.model.LeaveRecord;

import java.util.List;

public interface LeaveRepository {

    boolean save(LeaveRecord leaveRecord) throws Exception;

    List<LeaveRecord> findAll() throws Exception;

//    List<LeaveRecord> getLeaveStatusByIcNumber(Long userId) throws Exception;

    LeaveRecord findById(Long id) throws Exception;

    boolean updateLeaveStatus(Long leaveRecordId, String status) throws Exception;

//     List<LeaveRecord> findByIcNumber(String icNumber) throws Exception;
    List<LeaveRecord> findByUserId(Long userId) throws Exception;
}
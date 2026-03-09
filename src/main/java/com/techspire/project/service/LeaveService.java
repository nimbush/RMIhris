package com.techspire.project.service;

import com.techspire.project.dto.LeaveApplyDto;
import com.techspire.project.model.LeaveRecord;

import java.util.List;

public interface LeaveService {

    String applyLeave(LeaveApplyDto dto) throws Exception;

    boolean approveLeave(Long leaveRecordId) throws Exception;

    boolean rejectLeave(Long leaveRecordId) throws Exception;

    List<LeaveRecord> checkStatus() throws Exception;

    List<LeaveRecord> getLeaveStatusByIcNumber(String icNumber) throws Exception;
}
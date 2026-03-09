package com.techspire.project.service.impl;

import com.techspire.project.dto.LeaveApplyDto;
import com.techspire.project.exception.LeaveException;
import com.techspire.project.exception.UserNotFoundException;
import com.techspire.project.model.LeaveRecord;
import com.techspire.project.model.User;
import com.techspire.project.model.enums.LeaveStatus;
import com.techspire.project.repository.LeaveRepository;
import com.techspire.project.repository.UserRepository;
import com.techspire.project.repository.impl.LeaveRepositoryImpl;
import com.techspire.project.repository.impl.UserRepositoryImpl;
import com.techspire.project.service.LeaveService;

import java.time.LocalDate;
import java.util.List;

public class LeaveServiceImpl implements LeaveService {

    private final UserRepository userRepository;
    private final LeaveRepository leaveRepository;

    public LeaveServiceImpl() {
        this.userRepository = new UserRepositoryImpl();
        this.leaveRepository = new LeaveRepositoryImpl();
    }

    @Override
    public String applyLeave(LeaveApplyDto dto) throws Exception {
        if (dto == null) throw new IllegalArgumentException("LeaveApplyDto cannot be null");
        if (dto.getIcNumber() == null || dto.getIcNumber().trim().isEmpty()) throw new IllegalArgumentException("IC number is required");
        if (dto.getLeaveDays() <= 0) throw new LeaveException("Leave days must be greater than zero");

        User user = userRepository.findByIcNumber(dto.getIcNumber());
        if (user == null) throw new UserNotFoundException("User not found with IC number: " + dto.getIcNumber());

        if (dto.getLeaveDays() > user.getLeaveCount()) {
            return "LEAVE_INVALID";
        }

        LeaveRecord leaveRecord = new LeaveRecord();
        leaveRecord.setUserId(user.getId());
        leaveRecord.setLeaveDays(dto.getLeaveDays());
        leaveRecord.setStatus(LeaveStatus.PENDING);
        leaveRecord.setAppliedDate(LocalDate.now());

        boolean saved = leaveRepository.save(leaveRecord);
        return saved ? "LEAVE_APPLIED_SUCCESSFULLY" : "LEAVE_APPLICATION_FAILED";
    }

    @Override
    public boolean approveLeave(Long leaveRecordId) throws Exception {
        LeaveRecord record = leaveRepository.findById(leaveRecordId);
        if (record == null) throw new LeaveException("Leave record not found");
        if (record.getStatus() != LeaveStatus.PENDING) throw new LeaveException("Only pending leave can be approved");

        User user = userRepository.findById(record.getUserId());
        if (user == null) throw new UserNotFoundException("User not found");

        if (record.getLeaveDays() > user.getLeaveCount()) {
            throw new LeaveException("Insufficient leave balance");
        }

        int remainingLeave = user.getLeaveCount() - record.getLeaveDays();

        boolean updated = userRepository.updateLeaveCount(user.getId(), remainingLeave);
        if (!updated) return false;

        return leaveRepository.updateLeaveStatus(leaveRecordId, "APPROVED");
    }

    @Override
    public boolean rejectLeave(Long leaveRecordId) throws Exception {
        LeaveRecord record = leaveRepository.findById(leaveRecordId);
        if (record == null) throw new LeaveException("Leave record not found");
        if (record.getStatus() != LeaveStatus.PENDING) throw new LeaveException("Only pending leave can be rejected");

        return leaveRepository.updateLeaveStatus(leaveRecordId, "REJECTED");
    }

    @Override
    public List<LeaveRecord> checkStatus() throws Exception {
        return leaveRepository.findAll();
    }

    @Override
    public List<LeaveRecord> getLeaveStatusByIcNumber(String icNumber) throws Exception {
        return leaveRepository.findByIcNumber(icNumber);
    }
}
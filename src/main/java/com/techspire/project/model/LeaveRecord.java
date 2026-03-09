package com.techspire.project.model;

import com.techspire.project.model.enums.LeaveStatus;

import java.io.Serializable;
import java.time.LocalDate;

public class LeaveRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long userId;
    private int leaveDays;
    private LeaveStatus status;
    private LocalDate appliedDate;

    public LeaveRecord() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getLeaveDays() {
        return leaveDays;
    }

    public void setLeaveDays(int leaveDays) {
        this.leaveDays = leaveDays;
    }

    public LeaveStatus getStatus() {
        return status;
    }

    public void setStatus(LeaveStatus status) {
        this.status = status;
    }

    public LocalDate getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(LocalDate appliedDate) {
        this.appliedDate = appliedDate;
    }
}
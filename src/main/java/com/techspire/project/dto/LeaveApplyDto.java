package com.techspire.project.dto;

import java.io.Serializable;

public class LeaveApplyDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String icNumber;
    private int leaveDays;

    public LeaveApplyDto() {
    }

    public LeaveApplyDto(String icNumber, int leaveDays) {
        this.icNumber = icNumber;
        this.leaveDays = leaveDays;
    }

    public String getIcNumber() {
        return icNumber;
    }

    public void setIcNumber(String icNumber) {
        this.icNumber = icNumber;
    }

    public int getLeaveDays() {
        return leaveDays;
    }

    public void setLeaveDays(int leaveDays) {
        this.leaveDays = leaveDays;
    }
}
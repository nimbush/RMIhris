package com.techspire.project.dto;

import java.io.Serializable;
import java.time.LocalDate;

public class LeaveApplyDto implements Serializable {

    private String icNumber;
    private LocalDate fromDate;
    private LocalDate toDate;
    private int leaveDays;

    public LeaveApplyDto() {
    }

    public String getIcNumber() {
        return icNumber;
    }

    public void setIcNumber(String icNumber) {
        this.icNumber = icNumber;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public int getLeaveDays() {
        return leaveDays;
    }

    public void setLeaveDays(int leaveDays) {
        this.leaveDays = leaveDays;
    }
}
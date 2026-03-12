package com.techspire.project.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class UserDetailedReportDto implements Serializable {

    private Long userId;
    private String fullName;
    private String icNumber;
    private String userType;
    private int remainingLeave;
    private int totalLeave;
    private int totalApplications;
    private List<LeaveHistoryItemDto> leaveHistory;

    public UserDetailedReportDto() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getIcNumber() {
        return icNumber;
    }

    public void setIcNumber(String icNumber) {
        this.icNumber = icNumber;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public int getRemainingLeave() {
        return remainingLeave;
    }

    public void setRemainingLeave(int remainingLeave) {
        this.remainingLeave = remainingLeave;
    }

    public int getTotalLeave() {
        return totalLeave;
    }

    public void setTotalLeave(int totalLeave) {
        this.totalLeave = totalLeave;
    }

    public int getTotalApplications() {
        return totalApplications;
    }

    public void setTotalApplications(int totalApplications) {
        this.totalApplications = totalApplications;
    }

    public List<LeaveHistoryItemDto> getLeaveHistory() {
        return leaveHistory;
    }

    public void setLeaveHistory(List<LeaveHistoryItemDto> leaveHistory) {
        this.leaveHistory = leaveHistory;
    }

    public static class LeaveHistoryItemDto implements Serializable {
        private LocalDate fromDate;
        private LocalDate toDate;
        private int leaveDays;
        private String status;
        private LocalDate appliedDate;

        public LeaveHistoryItemDto() {
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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public LocalDate getAppliedDate() {
            return appliedDate;
        }

        public void setAppliedDate(LocalDate appliedDate) {
            this.appliedDate = appliedDate;
        }
    }
}
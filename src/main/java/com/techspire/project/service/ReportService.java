package com.techspire.project.service;

import com.techspire.project.dto.UserDetailedReportDto;
import com.techspire.project.dto.UserReportDto;

import java.util.List;

public interface ReportService {
    List<UserReportDto> yearlyReport() throws Exception;
    UserDetailedReportDto generateUserReport(String icNumber) throws Exception;
}
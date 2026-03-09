package com.techspire.project.service.impl;

import com.techspire.project.dto.UserReportDto;
import com.techspire.project.repository.UserRepository;
import com.techspire.project.repository.impl.UserRepositoryImpl;
import com.techspire.project.service.ReportService;

import java.util.List;

public class ReportServiceImpl implements ReportService {

    private final UserRepository userRepository;

    public ReportServiceImpl() {
        this.userRepository = new UserRepositoryImpl();
    }

    @Override
    public List<UserReportDto> yearlyReport() throws Exception {
        return userRepository.getYearlyReport();
    }
}
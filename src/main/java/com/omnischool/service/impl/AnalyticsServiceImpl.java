package com.omnischool.service.impl;

import com.omnischool.dto.DashboardStatsDTO;
import com.omnischool.service.AnalyticsService;
import com.omnischool.service.DemoRequestService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AnalyticsServiceImpl implements AnalyticsService {

    private final DemoRequestService demoRequestService;

    public AnalyticsServiceImpl(DemoRequestService demoRequestService) {
        this.demoRequestService = demoRequestService;
    }

    @Override
    public DashboardStatsDTO getDashboardStats(LocalDate startDate, LocalDate endDate) {
        return demoRequestService.calculateStats(startDate, endDate);
    }
}


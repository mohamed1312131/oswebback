package com.omnischool.service;

import com.omnischool.dto.DashboardStatsDTO;

import java.time.LocalDate;

public interface AnalyticsService {
    DashboardStatsDTO getDashboardStats(LocalDate startDate, LocalDate endDate);
}


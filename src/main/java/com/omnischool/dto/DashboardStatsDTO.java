package com.omnischool.dto;

import com.omnischool.enums.DemoStatus;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardStatsDTO {
    private long totalDemoRequests;
    private long pendingRequests;
    private long completedRequests;
    private double conversionRate;
    private List<DailyTrendDTO> dailyTrends;
    private Map<DemoStatus, Long> requestsByStatus;
}


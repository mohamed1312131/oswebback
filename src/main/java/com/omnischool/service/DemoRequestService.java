package com.omnischool.service;

import com.omnischool.dto.DashboardStatsDTO;
import com.omnischool.dto.DemoRequestDTO;
import com.omnischool.enums.DemoStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DemoRequestService {

    DemoRequestDTO createDemoRequest(DemoRequestDTO dto);

    Page<DemoRequestDTO> getFilteredRequests(DemoStatus status, String search, Pageable pageable);

    DemoRequestDTO getById(Long id);

    DemoRequestDTO updateStatus(Long id, DemoStatus status);

    void delete(Long id);

    DashboardStatsDTO calculateStats(java.time.LocalDate startDate, java.time.LocalDate endDate);
}


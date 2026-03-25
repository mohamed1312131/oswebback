package com.omnischool.service.impl;

import com.omnischool.dto.DashboardStatsDTO;
import com.omnischool.dto.DailyTrendDTO;
import com.omnischool.dto.DemoRequestDTO;
import com.omnischool.entity.DemoRequest;
import com.omnischool.enums.DemoStatus;
import com.omnischool.exception.ResourceNotFoundException;
import com.omnischool.repository.DemoRequestRepository;
import com.omnischool.service.DemoRequestService;
import com.omnischool.service.EmailService;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Demo requests service implementation.
 */
@Service
public class DemoRequestServiceImpl implements DemoRequestService {

    private final DemoRequestRepository repository;
    private final EmailService emailService;

    public DemoRequestServiceImpl(DemoRequestRepository repository, EmailService emailService) {
        this.repository = repository;
        this.emailService = emailService;
    }

    @Override
    public DemoRequestDTO createDemoRequest(DemoRequestDTO dto) {
        DemoRequest entity = toEntity(dto);
        entity.setId(null);
        entity.setStatus(DemoStatus.PENDING);

        DemoRequest saved = repository.save(entity);

        // Async emails
        emailService.sendDemoRequestConfirmation(saved);
        emailService.sendAdminNotification(saved);

        return toDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DemoRequestDTO> getFilteredRequests(DemoStatus status, String search, Pageable pageable) {
        String normalized = (search == null || search.isBlank()) ? null : search.trim();
        return repository.search(status, normalized, pageable)
                .map(e -> {
                    Hibernate.initialize(e.getInterestedIn());
                    return toDto(e);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public DemoRequestDTO getById(Long id) {
        DemoRequest entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DemoRequest not found"));
        Hibernate.initialize(entity.getInterestedIn());
        return toDto(entity);
    }

    @Override
    @Transactional
    public DemoRequestDTO updateStatus(Long id, DemoStatus status) {
        DemoRequest entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DemoRequest not found"));

        DemoStatus old = entity.getStatus();
        entity.setStatus(status);

        if (status == DemoStatus.SCHEDULED) {
            entity.setScheduledAt(LocalDateTime.now());
        }

        DemoRequest saved = repository.save(entity);
        Hibernate.initialize(saved.getInterestedIn());

        // Notify requester about status update (async, don't fail the update if email fails)
        try {
            emailService.sendDemoStatusUpdateNotification(saved, old, status);
        } catch (Exception e) {
            // Log the error but don't fail the update
            // TODO: log the exception
        }

        return toDto(saved);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("DemoRequest not found");
        }
        repository.deleteById(id);
    }

    @Override
    public DashboardStatsDTO calculateStats(LocalDate startDate, LocalDate endDate) {
        LocalDateTime start = (startDate == null)
                ? LocalDate.now().minusDays(30).atStartOfDay()
                : startDate.atStartOfDay();
        LocalDateTime end = (endDate == null)
                ? LocalDate.now().plusDays(1).atStartOfDay()
                : endDate.plusDays(1).atStartOfDay();

        List<DemoRequest> range = repository.findByCreatedAtBetween(start, end);

        long total = range.size();
        long pending = range.stream().filter(r -> r.getStatus() == DemoStatus.PENDING).count();
        long completed = range.stream().filter(r -> r.getStatus() == DemoStatus.SCHEDULED).count();
        double conversion = total == 0 ? 0.0 : (completed * 100.0) / total;

        Map<DemoStatus, Long> byStatus = new EnumMap<>(DemoStatus.class);
        for (DemoStatus s : DemoStatus.values()) {
            long count = range.stream().filter(r -> r.getStatus() == s).count();
            byStatus.put(s, count);
        }

        Map<LocalDate, List<DemoRequest>> perDay = new HashMap<>();
        for (DemoRequest r : range) {
            if (r.getCreatedAt() == null) continue;
            LocalDate day = r.getCreatedAt().toLocalDate();
            perDay.computeIfAbsent(day, d -> new ArrayList<>()).add(r);
        }

        List<DailyTrendDTO> trends = perDay.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> {
                    long req = e.getValue().size();
                    long conv = e.getValue().stream().filter(r -> r.getStatus() == DemoStatus.SCHEDULED).count();
                    return DailyTrendDTO.builder()
                            .date(e.getKey())
                            .requests(req)
                            .conversions(conv)
                            .build();
                })
                .toList();

        return DashboardStatsDTO.builder()
                .totalDemoRequests(total)
                .pendingRequests(pending)
                .completedRequests(completed)
                .conversionRate(conversion)
                .dailyTrends(trends)
                .requestsByStatus(byStatus)
                .build();
    }

    // -----------------------
    // Mapping helpers
    // -----------------------

    private static DemoRequestDTO toDto(DemoRequest e) {
        // Force initialization of the ElementCollection while the session is open
        // (prevents LazyInitializationException during JSON serialization)
        List<String> interests = (e.getInterestedIn() == null) ? List.of() : new ArrayList<>(e.getInterestedIn());

        return DemoRequestDTO.builder()
                .id(e.getId())
                .fullName(e.getFullName())
                .email(e.getEmail())
                .phone(e.getPhone())
                .schoolName(e.getSchoolName())
                .schoolType(e.getSchoolType())
                .city(e.getCity())
                .numberOfStudents(e.getNumberOfStudents())
                .level(e.getLevel())
                .currentSystem(e.getCurrentSystem())
                .preferredDate(e.getPreferredDate())
                .preferredTime(e.getPreferredTime())
                .interestedIn(interests)
                .message(e.getMessage())
                .status(e.getStatus())
                .assignedTo(e.getAssignedTo())
                .createdAt(e.getCreatedAt())
                .updatedAt(e.getUpdatedAt())
                .scheduledAt(e.getScheduledAt())
                .build();
    }

    private static DemoRequest toEntity(DemoRequestDTO d) {
        return DemoRequest.builder()
                .id(d.getId())
                .fullName(d.getFullName())
                .email(d.getEmail())
                .phone(d.getPhone())
                .schoolName(d.getSchoolName())
                .schoolType(d.getSchoolType())
                .city(d.getCity())
                .numberOfStudents(d.getNumberOfStudents())
                .level(d.getLevel())
                .currentSystem(d.getCurrentSystem())
                .preferredDate(d.getPreferredDate())
                .preferredTime(d.getPreferredTime())
                .interestedIn(d.getInterestedIn() == null ? new ArrayList<>() : new ArrayList<>(d.getInterestedIn()))
                .message(d.getMessage())
                .status(d.getStatus())
                .assignedTo(d.getAssignedTo())
                .scheduledAt(d.getScheduledAt())
                .build();
    }
}

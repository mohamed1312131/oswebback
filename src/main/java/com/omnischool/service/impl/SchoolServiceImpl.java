package com.omnischool.service.impl;

import com.omnischool.dto.SchoolDTO;
import com.omnischool.entity.School;
import com.omnischool.exception.ResourceNotFoundException;
import com.omnischool.repository.SchoolRepository;
import com.omnischool.service.SchoolService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class SchoolServiceImpl implements SchoolService {

    private final SchoolRepository repository;

    public SchoolServiceImpl(SchoolRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<SchoolDTO> getAll(Pageable pageable) {
        return repository.findAll(pageable).map(SchoolServiceImpl::toDto);
    }

    @Override
    public SchoolDTO create(SchoolDTO dto) {
        School saved = repository.save(toEntity(dto));
        return toDto(saved);
    }

    @Override
    public SchoolDTO update(Long id, SchoolDTO dto) {
        School existing = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("School not found"));

        existing.setName(dto.getName());
        existing.setAddress(dto.getAddress());
        existing.setCity(dto.getCity());
        existing.setPhone(dto.getPhone());
        existing.setEmail(dto.getEmail());
        existing.setType(dto.getType());
        existing.setTotalStudents(dto.getTotalStudents());
        existing.setTotalTeachers(dto.getTotalTeachers());
        existing.setPlan(dto.getPlan());
        existing.setSubscriptionStartDate(dto.getSubscriptionStartDate());
        existing.setSubscriptionEndDate(dto.getSubscriptionEndDate());
        existing.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : existing.getIsActive());

        return toDto(repository.save(existing));
    }

    private static School toEntity(SchoolDTO d) {
        return School.builder()
                .id(d.getId())
                .name(d.getName())
                .address(d.getAddress())
                .city(d.getCity())
                .phone(d.getPhone())
                .email(d.getEmail())
                .type(d.getType())
                .totalStudents(d.getTotalStudents())
                .totalTeachers(d.getTotalTeachers())
                .plan(d.getPlan())
                .subscriptionStartDate(d.getSubscriptionStartDate())
                .subscriptionEndDate(d.getSubscriptionEndDate())
                .isActive(d.getIsActive() != null ? d.getIsActive() : true)
                .build();
    }

    private static SchoolDTO toDto(School e) {
        return SchoolDTO.builder()
                .id(e.getId())
                .name(e.getName())
                .address(e.getAddress())
                .city(e.getCity())
                .phone(e.getPhone())
                .email(e.getEmail())
                .type(e.getType())
                .totalStudents(e.getTotalStudents())
                .totalTeachers(e.getTotalTeachers())
                .plan(e.getPlan())
                .subscriptionStartDate(e.getSubscriptionStartDate())
                .subscriptionEndDate(e.getSubscriptionEndDate())
                .isActive(e.getIsActive())
                .createdAt(e.getCreatedAt())
                .build();
    }
}


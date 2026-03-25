package com.omnischool.service;

import com.omnischool.dto.SchoolDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SchoolService {
    Page<SchoolDTO> getAll(Pageable pageable);

    SchoolDTO create(SchoolDTO dto);

    SchoolDTO update(Long id, SchoolDTO dto);
}


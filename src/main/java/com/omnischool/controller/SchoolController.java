package com.omnischool.controller;

import com.omnischool.dto.SchoolDTO;
import com.omnischool.service.SchoolService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schools")
public class SchoolController {

    private final SchoolService schoolService;

    public SchoolController(SchoolService schoolService) {
        this.schoolService = schoolService;
    }

    @GetMapping
    public ResponseEntity<Page<SchoolDTO>> getAll(Pageable pageable) {
        return ResponseEntity.ok(schoolService.getAll(pageable));
    }

    @PostMapping
    public ResponseEntity<SchoolDTO> create(@Valid @RequestBody SchoolDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(schoolService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SchoolDTO> update(@PathVariable Long id, @Valid @RequestBody SchoolDTO dto) {
        return ResponseEntity.ok(schoolService.update(id, dto));
    }
}


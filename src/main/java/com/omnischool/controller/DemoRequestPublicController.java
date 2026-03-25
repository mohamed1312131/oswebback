package com.omnischool.controller;

import com.omnischool.dto.DemoRequestDTO;
import com.omnischool.service.DemoRequestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/demo")
public class DemoRequestPublicController {

    private final DemoRequestService demoRequestService;

    public DemoRequestPublicController(DemoRequestService demoRequestService) {
        this.demoRequestService = demoRequestService;
    }

    @PostMapping("/request")
    public ResponseEntity<DemoRequestDTO> create(@Valid @RequestBody DemoRequestDTO dto) {
        DemoRequestDTO created = demoRequestService.createDemoRequest(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}


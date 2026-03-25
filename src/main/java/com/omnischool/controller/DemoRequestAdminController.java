package com.omnischool.controller;

import com.omnischool.dto.DemoRequestDTO;
import com.omnischool.enums.DemoStatus;
import com.omnischool.service.DemoRequestService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/demo/requests")
public class DemoRequestAdminController {

    private final DemoRequestService demoRequestService;

    public DemoRequestAdminController(DemoRequestService demoRequestService) {
        this.demoRequestService = demoRequestService;
    }

    @GetMapping
    public ResponseEntity<Page<DemoRequestDTO>> getAll(
            @RequestParam(required = false) DemoStatus status,
            @RequestParam(required = false) String search,
            Pageable pageable
    ) {
        return ResponseEntity.ok(demoRequestService.getFilteredRequests(status, search, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DemoRequestDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(demoRequestService.getById(id));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<DemoRequestDTO> updateStatus(@PathVariable Long id, @RequestParam DemoStatus status) {
        return ResponseEntity.ok(demoRequestService.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        demoRequestService.delete(id);
        return ResponseEntity.noContent().build();
    }
}


package com.omnischool.controller;

import com.omnischool.dto.ContactMessageDTO;
import com.omnischool.service.ContactMessageService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contact")
public class ContactPublicController {

    private final ContactMessageService contactMessageService;

    public ContactPublicController(ContactMessageService contactMessageService) {
        this.contactMessageService = contactMessageService;
    }

    @PostMapping("/submit")
    public ResponseEntity<ContactMessageDTO> submit(@Valid @RequestBody ContactMessageDTO dto) {
        ContactMessageDTO created = contactMessageService.submit(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}


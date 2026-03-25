package com.omnischool.controller;

import com.omnischool.dto.ContactMessageDTO;
import com.omnischool.enums.ContactMessageStatus;
import com.omnischool.service.ContactMessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contact/messages")
public class ContactAdminController {

    private final ContactMessageService contactMessageService;

    public ContactAdminController(ContactMessageService contactMessageService) {
        this.contactMessageService = contactMessageService;
    }

    @GetMapping
    public ResponseEntity<List<ContactMessageDTO>> getAll() {
        return ResponseEntity.ok(contactMessageService.getAll());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ContactMessageDTO> updateStatus(@PathVariable Long id, @RequestParam ContactMessageStatus status) {
        return ResponseEntity.ok(contactMessageService.updateStatus(id, status));
    }
}


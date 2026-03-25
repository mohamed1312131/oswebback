package com.omnischool.service.impl;

import com.omnischool.dto.ContactMessageDTO;
import com.omnischool.entity.ContactMessage;
import com.omnischool.enums.ContactMessageStatus;
import com.omnischool.exception.ResourceNotFoundException;
import com.omnischool.repository.ContactMessageRepository;
import com.omnischool.service.ContactMessageService;
import com.omnischool.service.EmailService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ContactMessageServiceImpl implements ContactMessageService {

    private final ContactMessageRepository repository;
    private final EmailService emailService;

    public ContactMessageServiceImpl(ContactMessageRepository repository, EmailService emailService) {
        this.repository = repository;
        this.emailService = emailService;
    }

    @Override
    public ContactMessageDTO submit(ContactMessageDTO dto) {
        ContactMessage saved = repository.save(toEntity(dto));
        emailService.sendContactConfirmation(saved);
        return toDto(saved);
    }

    @Override
    public List<ContactMessageDTO> getAll() {
        return repository.findAll().stream().map(ContactMessageServiceImpl::toDto).toList();
    }

    @Override
    public ContactMessageDTO updateStatus(Long id, ContactMessageStatus status) {
        ContactMessage entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ContactMessage not found"));
        entity.setStatus(status);
        if (status == ContactMessageStatus.READ) {
            entity.setReadAt(LocalDateTime.now());
        }
        return toDto(repository.save(entity));
    }

    private static ContactMessage toEntity(ContactMessageDTO d) {
        return ContactMessage.builder()
                .id(d.getId())
                .name(d.getName())
                .email(d.getEmail())
                .phone(d.getPhone())
                .subject(d.getSubject())
                .message(d.getMessage())
                .status(d.getStatus())
                .build();
    }

    private static ContactMessageDTO toDto(ContactMessage e) {
        return ContactMessageDTO.builder()
                .id(e.getId())
                .name(e.getName())
                .email(e.getEmail())
                .phone(e.getPhone())
                .subject(e.getSubject())
                .message(e.getMessage())
                .status(e.getStatus())
                .createdAt(e.getCreatedAt())
                .readAt(e.getReadAt())
                .build();
    }
}


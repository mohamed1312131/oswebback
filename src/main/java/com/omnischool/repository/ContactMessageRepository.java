package com.omnischool.repository;

import com.omnischool.entity.ContactMessage;
import com.omnischool.enums.ContactMessageStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {
    long countByStatus(ContactMessageStatus status);

    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}


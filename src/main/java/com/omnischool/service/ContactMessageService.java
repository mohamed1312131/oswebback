package com.omnischool.service;

import com.omnischool.dto.ContactMessageDTO;
import com.omnischool.enums.ContactMessageStatus;

import java.util.List;

public interface ContactMessageService {

    ContactMessageDTO submit(ContactMessageDTO dto);

    List<ContactMessageDTO> getAll();

    ContactMessageDTO updateStatus(Long id, ContactMessageStatus status);
}


package com.omnischool.service;

import com.omnischool.entity.ContactMessage;
import com.omnischool.entity.DemoRequest;
import com.omnischool.enums.DemoStatus;

public interface EmailService {
    void sendDemoRequestConfirmation(DemoRequest request);

    void sendAdminNotification(DemoRequest request);

    void sendContactConfirmation(ContactMessage message);

    void sendDemoStatusUpdateNotification(DemoRequest request, DemoStatus oldStatus, DemoStatus newStatus);
}


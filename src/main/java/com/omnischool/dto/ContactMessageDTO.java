package com.omnischool.dto;

import com.omnischool.enums.ContactMessageStatus;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactMessageDTO {

    private Long id;

    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String email;

    private String phone;

    @NotBlank
    private String subject;

    @NotBlank
    @Size(max = 2000)
    private String message;

    private ContactMessageStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime readAt;
}


package com.omnischool.dto;

import com.omnischool.enums.DemoStatus;
import com.omnischool.enums.EducationLevel;
import com.omnischool.enums.SchoolType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DemoRequestDTO {

    private Long id;

    @NotBlank
    private String fullName;

    @Email
    @NotBlank
    private String email;

    /** Tunisian phone number (8 digits). */
    @Pattern(regexp = "^\\d{8}$", message = "Phone must be exactly 8 digits")
    private String phone;

    @NotBlank
    private String schoolName;

    @NotNull
    private SchoolType schoolType;

    @Min(value = 50, message = "numberOfStudents must be at least 50")
    private Integer numberOfStudents;

    private EducationLevel level;

    private String currentSystem;

    private LocalDate preferredDate;

    private String preferredTime;

    private List<String> interestedIn;

    @Size(max = 2000)
    private String message;

    private DemoStatus status;

    private String assignedTo;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime scheduledAt;

    /** City of the school (from public booking form). */
    private String city;
}

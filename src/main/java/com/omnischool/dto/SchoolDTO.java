package com.omnischool.dto;

import com.omnischool.enums.SchoolPlan;
import com.omnischool.enums.SchoolType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchoolDTO {

    private Long id;

    @NotBlank
    private String name;

    private String address;

    private String city;

    private String phone;

    private String email;

    @NotNull
    private SchoolType type;

    private Integer totalStudents;

    private Integer totalTeachers;

    private SchoolPlan plan;

    private LocalDate subscriptionStartDate;

    private LocalDate subscriptionEndDate;

    private Boolean isActive;

    private LocalDateTime createdAt;
}


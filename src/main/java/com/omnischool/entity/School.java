package com.omnischool.entity;

import com.omnischool.enums.SchoolPlan;
import com.omnischool.enums.SchoolType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * School entity.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "schools")
public class School {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String name;

    @Column(columnDefinition = "VARCHAR(500)")
    private String address;

    @Column(columnDefinition = "VARCHAR(255)")
    private String city;

    @Column(columnDefinition = "VARCHAR(50)")
    private String phone;

    @Column(columnDefinition = "VARCHAR(255)")
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SchoolType type;

    private Integer totalStudents;

    private Integer totalTeachers;

    @Enumerated(EnumType.STRING)
    private SchoolPlan plan;

    private LocalDate subscriptionStartDate;

    private LocalDate subscriptionEndDate;

    @Builder.Default
    @Column(nullable = false)
    private Boolean isActive = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (isActive == null) isActive = true;
    }
}

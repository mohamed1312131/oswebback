package com.omnischool.entity;

import com.omnischool.enums.DemoStatus;
import com.omnischool.enums.EducationLevel;
import com.omnischool.enums.SchoolType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Demo request submitted from the public website.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "demo_requests")
public class DemoRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String fullName;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String email;

    @Column(columnDefinition = "VARCHAR(50)")
    private String phone;

    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    private String schoolName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SchoolType schoolType;

    private Integer numberOfStudents;

    @Enumerated(EnumType.STRING)
    private EducationLevel level;

    @Column(columnDefinition = "VARCHAR(255)")
    private String currentSystem;

    private LocalDate preferredDate;

    @Column(columnDefinition = "VARCHAR(50)")
    private String preferredTime;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "demo_request_interests", joinColumns = @JoinColumn(name = "demo_request_id"))
    @Column(name = "interest", columnDefinition = "VARCHAR(255)")
    @Builder.Default
    private List<String> interestedIn = new ArrayList<>();

    @Column(length = 2000, columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private DemoStatus status = DemoStatus.PENDING;
    /** City of the school (from public booking form). */
    @Column(columnDefinition = "VARCHAR(255)")
    private String city;

    /**
     * Name/email of the staff member responsible for handling this demo request.
     * Used by admin dashboard assignment workflows.
     */
    @Column(columnDefinition = "VARCHAR(255)")
    private String assignedTo;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    private LocalDateTime scheduledAt;

    @PrePersist
    void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (status == null) status = DemoStatus.PENDING;
    }

    @PreUpdate
    void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

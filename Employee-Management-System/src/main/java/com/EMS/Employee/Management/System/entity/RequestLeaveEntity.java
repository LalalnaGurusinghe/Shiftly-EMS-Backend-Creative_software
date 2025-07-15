package com.EMS.Employee.Management.System.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "request_leaves")
public class RequestLeaveEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "leave_id")
    private Integer leaveId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "leave_type")
    private String leaveType;

    @Column(name = "leave_from")
    private java.time.LocalDate leaveFrom;

    @Column(name = "leave_to")
    private java.time.LocalDate leaveTo;

    @Column(name = "duration_days")
    private int duration;

    @Column(name = "cover_person")
    private String coverPerson;

    @Column(name = "report_to")
    private String reportTo;

    @Column(name = "reason_for_leave")
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "leave_status")
    private LeaveStatus status = LeaveStatus.PENDING;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "created_at")
    private java.time.LocalDateTime createdAt;

    @Column(name = "updated_at")
    private java.time.LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = java.time.LocalDateTime.now();
        this.updatedAt = java.time.LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = java.time.LocalDateTime.now();
    }
}
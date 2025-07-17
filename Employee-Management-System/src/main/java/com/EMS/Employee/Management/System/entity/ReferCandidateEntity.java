package com.EMS.Employee.Management.System.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "refer_candidates")
@Data
public class ReferCandidateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "vacancy_id", nullable = false)
    private VacancyEntity vacancy;

    @Column(name = "applicant_name", nullable = false, length = 100)
    private String applicantName;

    @Column(name = "applicant_email", nullable = false, length = 100)
    private String applicantEmail;

    @Column(name = "message", length = 1000)
    private String message;

    @Column(name = "resume_file_name")
    private String resumeFileName;

    @Column(name = "resume_file_path")
    private String resumeFilePath;

    @ManyToOne(optional = false)
    @JoinColumn(name = "referred_by", nullable = false)
    private User referredBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReferStatus status = ReferStatus.UNREAD;
} 
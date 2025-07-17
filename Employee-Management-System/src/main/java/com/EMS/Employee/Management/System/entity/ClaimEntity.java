package com.EMS.Employee.Management.System.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "claims")
@Data
public class ClaimEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "claim_type", nullable = false, length = 100)
    private String claimType;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_path")
    private String filePath;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ClaimStatus status = ClaimStatus.PENDING;

    @ManyToOne(optional = false)
    @JoinColumn(name = "requested_by", nullable = false)
    private User requestedBy;
} 
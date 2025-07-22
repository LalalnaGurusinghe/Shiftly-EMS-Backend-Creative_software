package com.EMS.Employee.Management.System.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "timesheets")
@Data
public class Timesheet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String date;

    @Column(nullable = false)
    private String mode;

    @Column(nullable = false)
    private String activity;

    @Column(nullable = false)
    private double hours;

    @Column(nullable = false)
    private String status; // e.g., PENDING, ACCEPTED, REJECTED
} 
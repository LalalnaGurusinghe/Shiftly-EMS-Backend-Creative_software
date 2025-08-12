package com.EMS.Employee.Management.System.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "timesheets")
@Data
public class Timesheet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeEntity employee;

    @Column(nullable = false)
    private String date;

    @Column(nullable = false)
    private String mode;

    @Column(nullable = false)
    private String activity;

    @Column(nullable = false)
    private double hours;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private TimeSheetStatus status = TimeSheetStatus.PENDING;
} 
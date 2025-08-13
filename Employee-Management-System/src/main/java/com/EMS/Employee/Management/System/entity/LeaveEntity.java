package com.EMS.Employee.Management.System.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "leaves")
@Data
public class LeaveEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeEntity employee;

    @Column(name = "leave_type", nullable = false)
    private String leaveType;

    @Column(name = "leave_from", nullable = false)
    private String leaveFrom;

    @Column(name = "leave_to", nullable = false)
    private String leaveTo;

    @ManyToOne
    @JoinColumn(name = "cover_person_id",nullable = false)
    private EmployeeEntity coverPerson;

    @Column(name = "reason", length = 500,nullable = false)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "leave_status", nullable = false)
    private LeaveStatus status = LeaveStatus.PENDING;

   
} 
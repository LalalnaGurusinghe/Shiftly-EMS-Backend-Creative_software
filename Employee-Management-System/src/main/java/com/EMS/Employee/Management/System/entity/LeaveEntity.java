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
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "leave_type", nullable = false)
    private String leaveType;

    @Column(name = "leave_from", nullable = false)
    private LocalDate leaveFrom;

    @Column(name = "leave_to", nullable = false)
    private LocalDate leaveTo;

  

    // Cover person: Employee from same department
    @ManyToOne
    @JoinColumn(name = "cover_person_id")
    private EmployeeEntity coverPerson;

    // Report to: auto-filled from Employee table
    @ManyToOne
    @JoinColumn(name = "report_to_id")
    private EmployeeEntity reportTo;

    @Column(name = "reason", length = 500)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "leave_status", nullable = false)
    private LeaveStatus leaveStatus = LeaveStatus.PENDING;

   
} 
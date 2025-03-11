package com.EMS.Employee.Management.System.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "check_leaves")
public class CheckLeaveEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "check_id")
    private int checkId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leave_id", referencedColumnName = "leave_id")
    private RequestLeaveEntity requestLeave;


    @Enumerated(EnumType.STRING)
    @Column(name = "leave_status")
    private LeaveStatus status = LeaveStatus.PENDING;

    @Column(name = "admin_id")
    private int adminId;

    @Column(name = "admin_name")
    private String adminName;


}

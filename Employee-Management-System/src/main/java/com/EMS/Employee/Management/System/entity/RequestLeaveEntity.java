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

        @Column(name = "user_id")
        private Long userId; // Updated to Long

        @Column(name = "user_name")
        private String userName;

        @Column(name = "leave_type")
        private String leaveType;

        @Column(name = "leave_from")
        private String leaveFrom;

        @Column(name = "leave_to")
        private String leaveTo;

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
}
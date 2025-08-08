package com.EMS.Employee.Management.System.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "designations")
@Data
public class DesignationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "designation_name", nullable = false, length = 100)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "department_id")
    private DepartmentEntity department;
}
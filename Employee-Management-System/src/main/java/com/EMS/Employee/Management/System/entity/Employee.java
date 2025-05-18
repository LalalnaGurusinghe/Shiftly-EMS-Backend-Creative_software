package com.EMS.Employee.Management.System.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id", nullable = false)
    private Long Id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "gender", nullable = false)
    private String gender;

    @Column(name = "epf_no", nullable = false)
    private String epfNo;

    @Column(name = "employee_no", nullable = false, unique = true)
    private String employeeNo;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "designation", nullable = false)
    private String designation;

    @Column(name = "location")
    private String location;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "reporting_person")
    private String reportingPerson;
}

package com.EMS.Employee.Management.System.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "employees")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private int employeeId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "gender", nullable = false, length = 10)
    private String gender;

    @Column(name = "dob", nullable = false)
    private String dob;

    @Column(name = "location", nullable = false, length = 100)
    private String location;

    @Column(name = "skills", length = 1000)
    private String skills;

    @Column(name = "education", length = 1000)
    private String education;
}
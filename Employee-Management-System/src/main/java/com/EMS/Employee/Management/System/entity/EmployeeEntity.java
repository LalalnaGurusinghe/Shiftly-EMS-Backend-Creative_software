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

    @ManyToOne(optional = false)
    @JoinColumn(name = "departmentid")
    private DepartmentEntity department;

    // Add to EmployeeEntity.java
    @Column(name = "designation", length = 100)
    private String designation;

    @Column(name = "reporting_person", length = 100)
    private String reportingPerson;

    @Column(name = "reporting_person_email", length = 100)
    private String reportingPersonEmail;

    @Column(name = "team_name", length = 100)
    private String teamName;

    @ElementCollection
    @CollectionTable(name = "employee_skills", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "skill")
    private java.util.List<String> skills = new java.util.ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "employee_education", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "education")
    private java.util.List<String> education = new java.util.ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "employee_experience", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "experience")
    private java.util.List<String> experience = new java.util.ArrayList<>();
}

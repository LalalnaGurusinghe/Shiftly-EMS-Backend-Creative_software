package com.EMS.Employee.Management.System.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "first_name",length = 50)
    private String firstName;

    @Column(name = "last_name",length = 50)
    private String lastName;

    @Column(name = "gender", length = 10)
    private String gender;

    @Column(name = "dob")
    private String dob;

    @Column(name = "location",length = 100)
    private String location;

    @ManyToOne(optional = false)
    @JoinColumn(name = "departmentid")
    private DepartmentEntity department;

    // Add to EmployeeEntity.java
    @Column(name = "designation", length = 100)
    private String designation;

    @OneToOne
    @JoinColumn(name = "reporting_person_id", nullable = false)
    private User reportingPerson;

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

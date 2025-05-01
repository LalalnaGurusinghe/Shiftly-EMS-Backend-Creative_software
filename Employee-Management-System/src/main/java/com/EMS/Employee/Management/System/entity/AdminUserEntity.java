package com.EMS.Employee.Management.System.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "admin_users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_no")
    private int employeeNo;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "gender", nullable = false, length = 10)
    private String gender;

    @Column(name = "date_of_birth", nullable = false, length = 10)
    private String birthOfDate;

    @Column(name = "location", nullable = false, length = 100)
    private String location;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "epf_no", nullable = false, unique = true)
    private int epfNO;

    @Column(name = "designation", nullable = false, length = 50)
    private String designation;

    @Column(name = "department", nullable = false, length = 50)
    private String department;

    @Column(name = "reporting_person", nullable = false, length = 100)
    private String reportingPerson;
}
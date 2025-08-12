package com.EMS.Employee.Management.System.repo;

import com.EMS.Employee.Management.System.entity.Timesheet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Time;
import java.util.List;

public interface TimesheetRepo extends JpaRepository<Timesheet, Long> {
    List<Timesheet> findByEmployee_Department_Id(Long departmentId);
    List<Timesheet> findByEmployee_EmployeeId(int employeeId);
} 
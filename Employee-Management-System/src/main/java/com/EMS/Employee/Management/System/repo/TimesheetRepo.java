package com.EMS.Employee.Management.System.repo;

import com.EMS.Employee.Management.System.entity.Timesheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TimesheetRepo extends JpaRepository<Timesheet, Long> {
    List<Timesheet> findByEmployee_EmployeeId(Integer employeeId);
    List<Timesheet> findByProject_ProjectId(Long projectId);
    boolean existsByEmployee_EmployeeIdAndDate(Integer employeeId, LocalDate date);
} 
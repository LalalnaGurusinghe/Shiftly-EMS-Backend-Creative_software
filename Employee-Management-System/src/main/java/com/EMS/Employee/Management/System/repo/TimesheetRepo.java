package com.EMS.Employee.Management.System.repo;

import com.EMS.Employee.Management.System.entity.Timesheet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TimesheetRepo extends JpaRepository<Timesheet, Long> {
    List<Timesheet> findByUserId(Long userId);
} 
package com.EMS.Employee.Management.System.repo;

import com.EMS.Employee.Management.System.entity.Timesheet;
import com.EMS.Employee.Management.System.entity.TimesheetStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TimesheetRepo extends JpaRepository<Timesheet, Long> {
    List<Timesheet> findByUserId(Long userId);
    List<Timesheet> findByStatus(TimesheetStatus status);
    List<Timesheet> findByUserIdAndStatus(Long userId, TimesheetStatus status);
    boolean existsByUserIdAndDate(Long userId, LocalDate date);
} 
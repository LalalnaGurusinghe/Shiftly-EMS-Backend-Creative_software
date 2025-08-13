package com.EMS.Employee.Management.System.repo;

import com.EMS.Employee.Management.System.entity.LeaveEntity;
import com.EMS.Employee.Management.System.entity.Timesheet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRepo extends JpaRepository<LeaveEntity, Long> {
    List<LeaveEntity> findByEmployee_Department_Id(Long departmentId);
    List<LeaveEntity> findByEmployee_EmployeeId(int employeeId);
} 
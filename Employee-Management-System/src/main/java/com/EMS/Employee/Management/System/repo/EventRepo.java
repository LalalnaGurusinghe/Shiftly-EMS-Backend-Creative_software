package com.EMS.Employee.Management.System.repo;

import com.EMS.Employee.Management.System.entity.ClaimEntity;
import com.EMS.Employee.Management.System.entity.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EventRepo extends JpaRepository<EventEntity, Long> {
    List<EventEntity> findByEmployee_EmployeeId(int employeeId);
    List<EventEntity> findByEmployee_Department_Id(Long departmentId);
}
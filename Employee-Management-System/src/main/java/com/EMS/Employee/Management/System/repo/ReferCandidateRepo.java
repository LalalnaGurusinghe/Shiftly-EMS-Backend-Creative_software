package com.EMS.Employee.Management.System.repo;

import com.EMS.Employee.Management.System.entity.ClaimEntity;
import com.EMS.Employee.Management.System.entity.ReferCandidateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReferCandidateRepo extends JpaRepository<ReferCandidateEntity, Long> {
    List<ReferCandidateEntity> findByEmployee_EmployeeId(int employeeId);
    List<ReferCandidateEntity> findByEmployee_Department_Id(Long departmentId);
}

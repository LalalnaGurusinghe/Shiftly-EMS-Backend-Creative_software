package com.EMS.Employee.Management.System.repo;

import com.EMS.Employee.Management.System.entity.LetterRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LetterRequestRepo extends JpaRepository<LetterRequest, Long> {
    List<LetterRequest> findByEmployee_EmployeeId(int employeeId);
    List<LetterRequest> findByEmployee_Department_Id(Long departmentId);
}

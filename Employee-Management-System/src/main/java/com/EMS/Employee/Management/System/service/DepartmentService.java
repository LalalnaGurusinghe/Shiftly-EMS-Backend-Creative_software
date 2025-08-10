package com.EMS.Employee.Management.System.service;

import com.EMS.Employee.Management.System.dto.DepartmentDTO;
import java.util.List;

public interface DepartmentService {
    List<DepartmentDTO> getAllDepartments();
    // List<DepartmentDTO> getAllDepartmentsWithAdmin();
    DepartmentDTO createDepartment(DepartmentDTO departmentDto);
    DepartmentDTO assignAdmin(Long userId, Long departmentId);
    void deleteDepartment(Long departmentId);
    DepartmentDTO getDepartmentIdByAdminUserId(Long adminUserId);
    
}
package com.EMS.Employee.Management.System.service;

import com.EMS.Employee.Management.System.dto.EmployeeDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface EmployeeService {
    ResponseEntity<EmployeeDTO> addEmployee(Long userId, EmployeeDTO employeeDTO);
    ResponseEntity<EmployeeDTO> updateEmployee(Long userId, EmployeeDTO employeeDTO);
    // List<EmployeeDTO> getAll();
    // ResponseEntity<EmployeeDTO> updateProfileFields(int id, EmployeeDTO employeeDTO);
    // ResponseEntity<EmployeeDTO> getUserById(int id);
    void deleteEmployeeByUserId(Long id);
    // ResponseEntity<EmployeeDTO> updateUserById(int id, EmployeeDTO employeeDTO);
    // List<EmployeeDTO> getEmployeesByDepartment(String department);
    String getEmployeeNameById(int id);
    EmployeeDTO getEmployeeByUserId(Long userId);
}
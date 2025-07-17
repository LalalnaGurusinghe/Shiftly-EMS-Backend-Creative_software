package com.EMS.Employee.Management.System.service;

import com.EMS.Employee.Management.System.dto.EmployeeDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface EmployeeService {
    ResponseEntity<EmployeeDTO> addUser(EmployeeDTO employeeDTO);
    List<EmployeeDTO> getAll();
    ResponseEntity<EmployeeDTO> getUserById(int id);
    ResponseEntity<EmployeeDTO> deleteUserById(int id);
    ResponseEntity<EmployeeDTO> updateUserById(int id, EmployeeDTO employeeDTO);
}
package com.EMS.Employee.Management.System.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.EMS.Employee.Management.System.dto.EmployeeDTO;
import com.EMS.Employee.Management.System.entity.EmployeeEntity;
import com.EMS.Employee.Management.System.entity.User;
import com.EMS.Employee.Management.System.repo.EmployeeRepo;
import com.EMS.Employee.Management.System.repo.UserRepo;
import com.EMS.Employee.Management.System.service.EmployeeService;
import com.EMS.Employee.Management.System.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/shiftly/ems/employee")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/add/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeDTO> addEmployee(
            @PathVariable Long userId,
            @Valid @RequestBody EmployeeDTO employeeDTO) {
        return employeeService.addEmployee(userId, employeeDTO);
    }

    @GetMapping("/by-department/{departmentId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<EmployeeDTO>>
    getEmployeesByDepartment(@PathVariable Long departmentId) {
    return
    ResponseEntity.ok(employeeService.getEmployeesByDepartment(departmentId));
    }


    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEmployeeByUserId(@PathVariable Long id) {
        employeeService.deleteEmployeeByUserId(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update/{id}")
     @PreAuthorize("hasRole('USER')")
     public ResponseEntity<EmployeeDTO> updateEmployeeById(@PathVariable int id,
     @Valid @RequestBody EmployeeDTO employeeDTO) {
     return employeeService.updateEmployeeById(id, employeeDTO);
     }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<EmployeeDTO> getEmployeeByUserId(@PathVariable Long userId) {
        EmployeeDTO employee = employeeService.getEmployeeByUserId(userId);
        return ResponseEntity.ok(employee);
    }

     @GetMapping("/name/{id}")
     @PreAuthorize(" hasRole('USER')")
     public ResponseEntity<?> getEmployeeNameById(@PathVariable int id) {
         String fullName = employeeService.getEmployeeNameById(id);
         if (fullName == null) {
             return ResponseEntity.notFound().build();
         }
         java.util.Map<String, String> result = new java.util.HashMap<>();
         result.put("fullName", fullName);
         return ResponseEntity.ok(result);
     }
}
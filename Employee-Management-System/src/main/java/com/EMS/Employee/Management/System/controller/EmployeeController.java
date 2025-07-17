package com.EMS.Employee.Management.System.controller;

import com.EMS.Employee.Management.System.dto.EmployeeDTO;
import com.EMS.Employee.Management.System.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shiftly/ems/employee")
@CrossOrigin(origins = "http://localhost:3000")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeDTO> addEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
        return employeeService.addUser(employeeDTO);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable int id) {
        return employeeService.getUserById(id);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeDTO> deleteEmployeeById(@PathVariable int id) {
        return employeeService.deleteUserById(id);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeDTO> updateEmployeeById(@PathVariable int id, @Valid @RequestBody EmployeeDTO employeeDTO) {
        return employeeService.updateUserById(id, employeeDTO);
    }

    @PutMapping("/self-update")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<EmployeeDTO> updateOwnInfo(@RequestBody EmployeeDTO employeeDTO) {
        // Assume employeeId is set in DTO and userId is validated by security context in real implementation
        return employeeService.updateUserById(employeeDTO.getEmployeeId(), employeeDTO);
    }
}
package com.EMS.Employee.Management.System.controller;

import com.EMS.Employee.Management.System.dto.DepartmentDTO;
import com.EMS.Employee.Management.System.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/shiftly/ems/departments")
@CrossOrigin(origins = "http://localhost:3000")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    // Get all departments (for dropdowns)
    @GetMapping("/all")
    public ResponseEntity<List<DepartmentDTO>> getAllDepartments() {
        List<DepartmentDTO> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(departments);
    }

    @PostMapping("/add")
    public ResponseEntity<DepartmentDTO> createDepartment(@RequestBody DepartmentDTO departmentdto){
        DepartmentDTO department = departmentService.createDepartment(departmentdto);
        return ResponseEntity.ok(department);
    }

    @PutMapping("/assign/{userId}/department/{departmentId}")
    public ResponseEntity<DepartmentDTO> assignAdmin(@PathVariable Long userId,@PathVariable Long departmentId) {
        DepartmentDTO department = departmentService.assignAdmin(userId, departmentId);
        return ResponseEntity.ok(department);
    }

    @GetMapping("/name/{departmentId}")
    public ResponseEntity<String> getDepartmentNameById(@PathVariable Long departmentId) {
        String name = departmentService.getDepartmentNameById(departmentId);
        return name != null ? ResponseEntity.ok(name) : ResponseEntity.notFound().build();
    }
} 
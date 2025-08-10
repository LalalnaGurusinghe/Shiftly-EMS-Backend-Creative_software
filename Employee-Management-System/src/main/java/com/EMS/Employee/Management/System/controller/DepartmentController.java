package com.EMS.Employee.Management.System.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.EMS.Employee.Management.System.dto.DepartmentDTO;
import com.EMS.Employee.Management.System.service.DepartmentService;

@RestController
@RequestMapping("/api/v1/shiftly/ems/departments")
@CrossOrigin(origins = "http://localhost:3000")
public class DepartmentController {

    private DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService){
        this.departmentService = departmentService;
    }

    // Get all departments (for dropdowns)
    @GetMapping("/all")
    public ResponseEntity<List<DepartmentDTO>> getAllDepartments() {
        List<DepartmentDTO> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(departments);
    }

    // @GetMapping("/with-admin")
    // public ResponseEntity<List<DepartmentDTO>> getAllDepartmentsWithAdmin() {
    //     List<DepartmentDTO> departments = departmentService.getAllDepartmentsWithAdmin();
    //     return ResponseEntity.ok(departments);
    // }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<DepartmentDTO> createDepartment(@RequestBody DepartmentDTO departmentdto){
        DepartmentDTO department = departmentService.createDepartment(departmentdto);
        return ResponseEntity.ok(department);
    }

    @PutMapping("/assign/{userId}/department/{departmentId}")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<DepartmentDTO> assignAdmin(@PathVariable Long userId,@PathVariable Long departmentId) {
        DepartmentDTO department = departmentService.assignAdmin(userId, departmentId);
        return ResponseEntity.ok(department);
    }


    @DeleteMapping("/delete/{departmentId}")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long departmentId) {
        departmentService.deleteDepartment(departmentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/by-admin/{adminUserId}")
    public ResponseEntity<DepartmentDTO> getDepartmentIdByAdminUserId(@PathVariable Long adminUserId) {
        DepartmentDTO department = departmentService.getDepartmentIdByAdminUserId(adminUserId);
        return department != null ? ResponseEntity.ok(department) : ResponseEntity.notFound().build();
    }

}
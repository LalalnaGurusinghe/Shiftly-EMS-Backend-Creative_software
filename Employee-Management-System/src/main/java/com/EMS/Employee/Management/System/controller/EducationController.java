package com.EMS.Employee.Management.System.controller;

import com.EMS.Employee.Management.System.dto.EducationDTO;
import com.EMS.Employee.Management.System.service.EducationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shiftly/ems/education")
@CrossOrigin(origins = "http://localhost:3000")
public class EducationController {
    private final EducationService educationService;

    public EducationController(EducationService educationService) {
        this.educationService = educationService;
    }

    // View all education for an employee (admin & employee)
    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<EducationDTO>> getEducationByEmployee(@PathVariable Integer employeeId) {
        return ResponseEntity.ok(educationService.getEducationByEmployeeId(employeeId));
    }

    // Add an education record (employee)
    @PostMapping("/add")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<EducationDTO> addEducation(@RequestBody EducationDTO educationDTO) {
        return ResponseEntity.ok(educationService.addEducation(educationDTO));
    }

    // Edit an education record (employee)
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<EducationDTO> updateEducation(@PathVariable Long id, @RequestBody EducationDTO educationDTO) {
        return ResponseEntity.ok(educationService.updateEducation(id, educationDTO));
    }

    // Delete an education record (employee)
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteEducation(@PathVariable Long id) {
        educationService.deleteEducation(id);
        return ResponseEntity.ok().build();
    }
} 
package com.EMS.Employee.Management.System.controller;

import java.util.List;

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

import com.EMS.Employee.Management.System.dto.DesignationDTO;
import com.EMS.Employee.Management.System.service.DesignationService;

@RestController
@RequestMapping("/api/v1/shiftly/ems/designations")
@CrossOrigin(origins = "http://localhost:3000")
@PreAuthorize("hasRole('ADMIN')")
public class DesignationController {

    private final DesignationService designationService;

    public DesignationController(DesignationService designationService) {
        this.designationService = designationService;
    }

    @PostMapping("/add/{departmentId}")
    public ResponseEntity<DesignationDTO> createDesignation(
            @PathVariable Long departmentId,
            @RequestBody DesignationDTO dto) {
        dto.setDepartmentId(departmentId);
        return ResponseEntity.ok(designationService.createDesignation(dto));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<DesignationDTO> updateDesignation(@PathVariable Long id, @RequestBody DesignationDTO dto) {
        return ResponseEntity.ok(designationService.updateDesignation(id, dto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteDesignation(@PathVariable Long id) {
        designationService.deleteDesignation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<DesignationDTO>> getAllDesignations() {
        return ResponseEntity.ok(designationService.getAllDesignations());
    }

    // @GetMapping("/{id}")
    // public ResponseEntity<DesignationDTO> getDesignationById(@PathVariable Long id) {
    //     return ResponseEntity.ok(designationService.getDesignationById(id));
    // }

    @GetMapping("/by-department/{departmentId}")
    public ResponseEntity<List<DesignationDTO>> getDesignationsByDepartmentId(@PathVariable Long departmentId) {
        return ResponseEntity.ok(designationService.getDesignationsByDepartmentId(departmentId));
    }
}
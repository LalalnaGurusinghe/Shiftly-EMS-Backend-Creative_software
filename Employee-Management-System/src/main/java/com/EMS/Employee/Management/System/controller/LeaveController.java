package com.EMS.Employee.Management.System.controller;

import com.EMS.Employee.Management.System.dto.LeaveDTO;
import com.EMS.Employee.Management.System.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/v1/shiftly/ems/leaves")
@CrossOrigin(origins = "http://localhost:3000")
public class LeaveController {
    @Autowired
    private LeaveService leaveService;

    // Employee: Apply for leave (with optional file upload)
    @PostMapping("/apply")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<LeaveDTO> applyLeave(@RequestBody LeaveDTO leaveDTO, Authentication authentication) {
        return ResponseEntity.ok(leaveService.applyLeave(leaveDTO, authentication.getName()));
    }

    // Employee: View own leaves
    @GetMapping("/my")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<LeaveDTO>> getOwnLeaves(Principal principal) {
        List<LeaveDTO> leaves = leaveService.getOwnLeaves(principal.getName());
        return ResponseEntity.ok(leaves);
    }

    // Employee: Update own leave
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<LeaveDTO> updateOwnLeave(@PathVariable Long id,
                                                   @RequestBody LeaveDTO leaveDTO,
                                                   Principal principal) {
        LeaveDTO result = leaveService.updateOwnLeave(id, leaveDTO, principal.getName());
        return ResponseEntity.ok(result);
    }

    // Employee: Delete own leave
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteOwnLeave(@PathVariable Long id, Principal principal) {
        leaveService.deleteOwnLeave(id, principal.getName());
        return ResponseEntity.noContent().build();
    }

    // Admin: View all leaves
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<LeaveDTO>> getAllLeaves() {
        List<LeaveDTO> leaves = leaveService.getAllLeaves();
        return ResponseEntity.ok(leaves);
    }

    // Admin: Approve/Reject/Update status
    @PutMapping("/status/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<LeaveDTO> updateLeaveStatus(@PathVariable Long id, @RequestParam String status) {
        LeaveDTO result = leaveService.updateLeaveStatus(id, status);
        return ResponseEntity.ok(result);
    }
} 
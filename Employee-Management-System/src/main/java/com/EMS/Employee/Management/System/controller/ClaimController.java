package com.EMS.Employee.Management.System.controller;

import com.EMS.Employee.Management.System.dto.ClaimDTO;
import com.EMS.Employee.Management.System.service.ClaimService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.Authentication;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/shiftly/ems/claims")
public class ClaimController {
    private final ClaimService claimService;

    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    @PostMapping(value = "/add/{employeeId}", consumes = { "multipart/form-data" })
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ClaimDTO> create(
            @PathVariable int employeeId,
            @RequestParam String claimType,
            @RequestParam String description,
            @RequestParam(required = false) MultipartFile file,
            @RequestParam String claimDate
            ) throws Exception {
        ClaimDTO dto = claimService.create(
                employeeId, claimType, description, file, claimDate);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("employee/{employeeId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ClaimDTO>> getByEmployeeId(@PathVariable int employeeId) {
        List<ClaimDTO> claims = claimService.getByEmployeeId(employeeId);
        return ResponseEntity.ok(claims);
    }

    @GetMapping("/admin/{adminUserId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ClaimDTO>> getClaimsForAdmin(@PathVariable Long adminUserId) {
        List<ClaimDTO> claims = claimService.getClaimsForAdmin(adminUserId);
        return ResponseEntity.ok(claims);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ClaimDTO>> getAllClaims() {
        return ResponseEntity.ok(claimService.getAllClaims());
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteClaim(@PathVariable Long id) {
        claimService.deleteClaim(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/status/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClaimDTO> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(claimService.updateStatus(id, status));
    }


    // Employee: Edit own claim
    @PutMapping(value = "/update/{id}", consumes = { "multipart/form-data" })
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ClaimDTO> updateClaim(
            @PathVariable Long id,
            @RequestParam(required = false) String claimType,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) MultipartFile file,
            @RequestParam(required = false) String claimDate) throws Exception {
        ClaimDTO dto = claimService.updateClaim(id, claimType, description, file, claimDate);
        return ResponseEntity.ok(dto);
    }
    
}
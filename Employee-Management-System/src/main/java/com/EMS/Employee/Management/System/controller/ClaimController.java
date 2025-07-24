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

@RestController
@RequestMapping("/api/v1/shiftly/ems/claims")
@CrossOrigin(origins = "http://localhost:3000")
public class ClaimController {
    private final ClaimService claimService;

    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    // Admin: View all claims
    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<ClaimDTO>> getAllClaims(Authentication authentication) {
        return ResponseEntity.ok(claimService.getAllClaims(authentication.getName()));
    }


    // Admin: Approve claim
    @PutMapping("/approve/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClaimDTO> approveClaim(@PathVariable Long id) {
        return ResponseEntity.ok(claimService.approveClaim(id));
    }

    // Admin: Reject claim
    @PutMapping("/reject/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClaimDTO> rejectClaim(@PathVariable Long id) {
        return ResponseEntity.ok(claimService.rejectClaim(id));
    }

    // Employee: Create claim
    @PostMapping(value = "/add", consumes = { "multipart/form-data" })
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ClaimDTO> createClaim(
            @RequestParam String claimType,
            @RequestParam String description,
            @RequestParam(required = false) MultipartFile file,
            @RequestParam(required = false) String status,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate claimDate,
            Authentication authentication) throws Exception {
        ClaimDTO dto = claimService.createClaim(
                claimType, description, file, status, authentication.getName(), claimDate);
        return ResponseEntity.ok(dto);
    }

    // Employee: Edit own claim
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ClaimDTO> updateClaim(@PathVariable Long id, @RequestBody ClaimDTO claimDTO,
            Authentication authentication) {
        return ResponseEntity.ok(claimService.updateClaim(id, claimDTO, authentication.getName()));
    }

    // Employee: Delete own claim
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteClaim(@PathVariable Long id, Authentication authentication) {
        claimService.deleteClaim(id, authentication.getName());
        return ResponseEntity.ok().build();
    }

    // Get claim by id (admin or employee)
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<ClaimDTO> getClaimById(@PathVariable Long id) {
        return ResponseEntity.ok(claimService.getClaimById(id));
    }

    // Get claims by user id (employee)
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ClaimDTO>> getClaimsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(claimService.getClaimsByUserId(userId));
    }
}
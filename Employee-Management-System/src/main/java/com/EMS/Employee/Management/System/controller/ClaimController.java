package com.EMS.Employee.Management.System.controller;

import com.EMS.Employee.Management.System.dto.ClaimDTO;
import com.EMS.Employee.Management.System.service.ClaimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.core.Authentication;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/shiftly/ems/claims")
@CrossOrigin(origins = "http://localhost:3000")
public class ClaimController {
    @Autowired
    private ClaimService claimService;

    // Employee: Create claim (with optional file upload)
    @PostMapping("/add")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ClaimDTO> createClaim(@RequestBody ClaimDTO dto, Authentication authentication) {
        return ResponseEntity.ok(claimService.createClaim(dto, authentication.getName()));
    }

    // Employee: View own claims
    @GetMapping("/my")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ClaimDTO>> getOwnClaims(Principal principal) {
        List<ClaimDTO> claims = claimService.getOwnClaims(principal.getName());
        return ResponseEntity.ok(claims);
    }

    // Employee: Update own claim
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ClaimDTO> updateOwnClaim(@PathVariable Long id,
                                                   @RequestBody ClaimDTO dto,
                                                   Principal principal) {
        ClaimDTO result = claimService.updateOwnClaim(id, dto, principal.getName());
        return ResponseEntity.ok(result);
    }

    // Employee: Delete own claim
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteOwnClaim(@PathVariable Long id, Principal principal) {
        claimService.deleteOwnClaim(id, principal.getName());
        return ResponseEntity.noContent().build();
    }

    // Admin: View all claims
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<ClaimDTO>> getAllClaims() {
        List<ClaimDTO> claims = claimService.getAllClaims();
        return ResponseEntity.ok(claims);
    }

    // Admin: Approve/Reject/Update status
    @PutMapping("/status/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ClaimDTO> updateClaimStatus(@PathVariable Long id, @RequestParam String status) {
        ClaimDTO result = claimService.updateClaimStatus(id, status);
        return ResponseEntity.ok(result);
    }
} 
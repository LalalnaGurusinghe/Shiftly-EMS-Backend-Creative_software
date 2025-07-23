package com.EMS.Employee.Management.System.controller;

import com.EMS.Employee.Management.System.dto.ReferCandidateDTO;
import com.EMS.Employee.Management.System.service.ReferCandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;

import java.security.Principal;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/shiftly/ems/referrals")
@CrossOrigin(origins = "http://localhost:3000")
public class ReferCandidateController {
    @Autowired
    private ReferCandidateService referCandidateService;

    // Employee: Create referral (with optional resume upload)
    @PostMapping(value = "/add", consumes = { "multipart/form-data" })
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ReferCandidateDTO> createReferral(
            @RequestParam Long vacancyId,
            @RequestParam String applicantName,
            @RequestParam String applicantEmail,
            @RequestParam(required = false) String message,
            @RequestParam(required = false) MultipartFile file,
            @RequestParam(required = false) String status,
            Authentication authentication) throws Exception {
        ReferCandidateDTO dto = referCandidateService.createReferral(
                vacancyId, applicantName, applicantEmail, message, file, status, authentication.getName());
        return ResponseEntity.ok(dto);
    }

    // Employee: View own referrals
    @GetMapping("/my")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ReferCandidateDTO>> getOwnReferrals(Principal principal) {
        List<ReferCandidateDTO> referrals = referCandidateService.getOwnReferrals(principal.getName());
        return ResponseEntity.ok(referrals);
    }

    // Employee: Update own referral
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ReferCandidateDTO> updateOwnReferral(@PathVariable Long id,
            @RequestBody ReferCandidateDTO dto,
            Principal principal) {
        ReferCandidateDTO result = referCandidateService.updateOwnReferral(id, dto, principal.getName());
        return ResponseEntity.ok(result);
    }

    // Employee: Delete own referral
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteOwnReferral(@PathVariable Long id, Principal principal) {
        referCandidateService.deleteOwnReferral(id, principal.getName());
        return ResponseEntity.noContent().build();
    }

    // Admin: View all referrals
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<ReferCandidateDTO>> getAllReferrals() {
        List<ReferCandidateDTO> referrals = referCandidateService.getAllReferrals();
        return ResponseEntity.ok(referrals);
    }

    // Admin: Update status (Read/Unread)
    @PutMapping("/status/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ReferCandidateDTO> updateReferralStatus(@PathVariable Long id, @RequestParam String status) {
        ReferCandidateDTO result = referCandidateService.updateReferralStatus(id, status);
        return ResponseEntity.ok(result);
    }

    // Get all referrals by userId
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ReferCandidateDTO>> getReferralsByUserId(@PathVariable Long userId) {
        List<ReferCandidateDTO> referrals = referCandidateService.getReferralsByUserId(userId);
        return ResponseEntity.ok(referrals);
    }
}
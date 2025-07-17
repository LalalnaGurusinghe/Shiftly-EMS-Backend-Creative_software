package com.EMS.Employee.Management.System.controller;

import com.EMS.Employee.Management.System.dto.ReferCandidateDTO;
import com.EMS.Employee.Management.System.service.ReferCandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/shiftly/ems/referrals")
@CrossOrigin(origins = "http://localhost:3000")
public class ReferCandidateController {
    @Autowired
    private ReferCandidateService referCandidateService;

    // Employee: Create referral (with optional resume upload)
    @PostMapping("/add")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ReferCandidateDTO> createReferral(@RequestPart("referral") ReferCandidateDTO dto,
                                                           @RequestPart(value = "resume", required = false) MultipartFile resume,
                                                           Principal principal) {
        // File handling logic (save file, set resumeFileName/resumeFilePath in dto) can be added here
        if (resume != null && !resume.isEmpty()) {
            dto.setResumeFileName(resume.getOriginalFilename());
            dto.setResumeFilePath("/uploads/resumes/" + resume.getOriginalFilename()); // Example path
        }
        ReferCandidateDTO result = referCandidateService.createReferral(dto, principal.getName());
        return ResponseEntity.ok(result);
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
} 
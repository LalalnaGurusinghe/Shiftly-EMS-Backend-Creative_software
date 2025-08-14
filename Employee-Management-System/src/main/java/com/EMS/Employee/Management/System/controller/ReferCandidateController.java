package com.EMS.Employee.Management.System.controller;

import com.EMS.Employee.Management.System.dto.ClaimDTO;
import com.EMS.Employee.Management.System.dto.ReferCandidateDTO;
import com.EMS.Employee.Management.System.service.ClaimService;
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

    private final ReferCandidateService referCandidateService;

    public ReferCandidateController(ReferCandidateService referCandidateService) {
        this.referCandidateService = referCandidateService;
    }

    @PostMapping(value = "/add/{employeeId}", consumes = { "multipart/form-data" })
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ReferCandidateDTO> createReferral(
            @PathVariable int employeeId,
            @RequestParam Long vacancyId,
            @RequestParam String applicantName,
            @RequestParam String applicantEmail,
            @RequestParam(required = false) String message,
            @RequestParam(required = false) MultipartFile file,
            @RequestParam(required = false) String status) throws Exception {
        ReferCandidateDTO dto = referCandidateService.create(
                employeeId,vacancyId, applicantName, applicantEmail, message, file);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("employee/{employeeId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ReferCandidateDTO>> getByEmployeeId(@PathVariable int employeeId) {
        List<ReferCandidateDTO> refers = referCandidateService.getByEmployeeId(employeeId);
        return ResponseEntity.ok(refers);
    }

    @GetMapping("/admin/{adminUserId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ReferCandidateDTO>> getRefersForAdmin(@PathVariable Long adminUserId) {
        List<ReferCandidateDTO> refers = referCandidateService.getRefersForAdmin(adminUserId);
        return ResponseEntity.ok(refers);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ReferCandidateDTO>> getAllRefers() {
        return ResponseEntity.ok(referCandidateService.getAllRefers());
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteClaim(@PathVariable Long id) {
        referCandidateService.deleteRefer(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/status/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReferCandidateDTO> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(referCandidateService.updateStatus(id, status));
    }

    @PutMapping(value = "/update/{id}", consumes = { "multipart/form-data" })
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ReferCandidateDTO> updateRefer(
            @PathVariable Long id,
            @RequestParam(required = false) Long vacancyId,
            @RequestParam(required = false) String applicantName,
            @RequestParam(required = false) String applicantEmail,
            @RequestParam(required = false) String message,
            @RequestParam(required = false) MultipartFile file) throws Exception {
        ReferCandidateDTO dto = referCandidateService.updateRefer(id, vacancyId, applicantName, applicantEmail,
                message, file);
        return ResponseEntity.ok(dto);
    }
}
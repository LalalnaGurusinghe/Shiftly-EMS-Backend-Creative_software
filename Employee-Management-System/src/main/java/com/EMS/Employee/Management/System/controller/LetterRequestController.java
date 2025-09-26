   
package com.EMS.Employee.Management.System.controller;

import com.EMS.Employee.Management.System.dto.LetterRequestDto;
import com.EMS.Employee.Management.System.service.LetterRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/shiftly/ems/letters")
public class LetterRequestController {
    private final LetterRequestService letterRequestService;

    public LetterRequestController(LetterRequestService letterRequestService) {
        this.letterRequestService = letterRequestService;
    }

    // User: Create a letter request
    @PostMapping("/add/{employeeId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<LetterRequestDto> createRequest(@PathVariable int employeeId, @RequestBody LetterRequestDto dto) {
        return ResponseEntity.ok(letterRequestService.createRequest(employeeId, dto));
    }

    // User: View own letter requests
    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<LetterRequestDto>> getByEmployeeId(@PathVariable int employeeId) {
        return ResponseEntity.ok(letterRequestService.getByEmployeeId(employeeId));
    }

    // Admin: View letter requests for admin's department
    @GetMapping("/admin/{adminUserId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<LetterRequestDto>> getRequestsForAdmin(@PathVariable Long adminUserId) {
        return ResponseEntity.ok(letterRequestService.getRequestsForAdmin(adminUserId));
    }

    // Admin: View all letter requests
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<LetterRequestDto>> getAllRequests() {
        return ResponseEntity.ok(letterRequestService.getAllRequests());
    }

    // Admin: Generate letter for a request
    @PostMapping("/generate/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> generateLetter(@PathVariable Long id) {
        return ResponseEntity.ok(letterRequestService.generateLetter(id));
    }

    // User: Delete own letter request
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteRequest(@PathVariable Long id) {
        letterRequestService.deleteRequest(id);
        return ResponseEntity.ok().build();
    }

    // Admin: Update letter request status
    @PutMapping("/status/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LetterRequestDto> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(letterRequestService.updateStatus(id, status));
    }

    // User: Update letter request
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<LetterRequestDto> updateRequest(@PathVariable Long id, @RequestBody LetterRequestDto dto) {
        return ResponseEntity.ok(letterRequestService.updateRequest(id, dto));
    }
}

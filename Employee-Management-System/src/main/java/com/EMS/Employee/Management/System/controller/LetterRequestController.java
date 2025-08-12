   
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
@CrossOrigin(origins = "http://localhost:3000")
public class LetterRequestController {
    private final LetterRequestService letterRequestService;

    public LetterRequestController(LetterRequestService letterRequestService) {
        this.letterRequestService = letterRequestService;
    }

    // User: Create a letter request
    @PostMapping("/request")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<LetterRequestDto> createRequest(@RequestBody LetterRequestDto dto, Principal principal) {
        return ResponseEntity.ok(letterRequestService.createRequest(dto, principal.getName()));
    }

    // User: View own letter requests
    @GetMapping("/my")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<LetterRequestDto>> getUserRequests(Principal principal) {
        return ResponseEntity.ok(letterRequestService.getUserRequests(principal.getName()));
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
    public ResponseEntity<Void> deleteRequest(@PathVariable Long id, Principal principal) {
        letterRequestService.deleteRequest(id, principal.getName());
        return ResponseEntity.ok().build();
    }
}

package com.EMS.Employee.Management.System.controller;

import com.EMS.Employee.Management.System.dto.CheckLeaveDTO;
import com.EMS.Employee.Management.System.entity.LeaveStatus;
import com.EMS.Employee.Management.System.service.CheckLeaveService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shiftly/ems/admin/check-leave")
@CrossOrigin(origins = "http://localhost:3000")
public class CheckLeaveController {
    private final CheckLeaveService checkLeaveService;

    public CheckLeaveController(CheckLeaveService checkLeaveService) {
        this.checkLeaveService = checkLeaveService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<CheckLeaveDTO>> getAllLeaveChecks() {
        return ResponseEntity.ok(checkLeaveService.getAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<CheckLeaveDTO> getLeaveById(@PathVariable int id) {
        return checkLeaveService.getLeaveById(id);
    }

    @PutMapping("/update-status/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<CheckLeaveDTO> updateLeaveStatus(
            @PathVariable int id,
            @RequestBody LeaveStatusDTO statusDTO) {
        String adminEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return checkLeaveService.updateLeaveStatus(id, statusDTO.getStatus(), adminEmail);
    }
}

// DTO for status update
class LeaveStatusDTO {
    private LeaveStatus status;

    public LeaveStatus getStatus() {
        return status;
    }

    public void setStatus(LeaveStatus status) {
        this.status = status;
    }
}
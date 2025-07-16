package com.EMS.Employee.Management.System.controller;

import com.EMS.Employee.Management.System.dto.TimesheetDTO;
import com.EMS.Employee.Management.System.service.TimesheetService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/timesheets")
public class TimesheetController {
    private final TimesheetService timesheetService;

    public TimesheetController(TimesheetService timesheetService) {
        this.timesheetService = timesheetService;
    }

    // Employee submits a timesheet
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/submit")
    public ResponseEntity<TimesheetDTO> submit(@Valid @RequestBody TimesheetDTO dto, Principal principal) {
        TimesheetDTO result = timesheetService.submitTimesheet(dto, principal.getName());
        return ResponseEntity.ok(result);
    }

    // Employee views their own timesheets
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/my")
    public ResponseEntity<List<TimesheetDTO>> myTimesheets(Principal principal) {
        List<TimesheetDTO> timesheets = timesheetService.getUserTimesheets(principal.getName());
        return ResponseEntity.ok(timesheets);
    }

    // Admin/Super Admin views all timesheets
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<TimesheetDTO>> allTimesheets() {
        List<TimesheetDTO> timesheets = timesheetService.getAllTimesheets();
        return ResponseEntity.ok(timesheets);
    }

    // Admin/Super Admin approves a timesheet
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @PostMapping("/{id}/approve")
    public ResponseEntity<TimesheetDTO> approve(@PathVariable Long id, Principal principal) {
        TimesheetDTO result = timesheetService.approveTimesheet(id, principal.getName());
        return ResponseEntity.ok(result);
    }

    // Admin/Super Admin rejects a timesheet
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @PostMapping("/{id}/reject")
    public ResponseEntity<TimesheetDTO> reject(@PathVariable Long id, Principal principal) {
        TimesheetDTO result = timesheetService.rejectTimesheet(id, principal.getName());
        return ResponseEntity.ok(result);
    }
} 
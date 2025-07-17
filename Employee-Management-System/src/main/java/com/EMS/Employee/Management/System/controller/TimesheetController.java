package com.EMS.Employee.Management.System.controller;

import com.EMS.Employee.Management.System.dto.TimesheetDTO;
import com.EMS.Employee.Management.System.service.TimesheetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/shiftly/ems/timesheets")
@CrossOrigin(origins = "http://localhost:3000")
public class TimesheetController {
    @Autowired
    private TimesheetService timesheetService;

    // Employee: Submit timesheet
    @PostMapping("/submit")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TimesheetDTO> submitTimesheet(@RequestBody TimesheetDTO dto, Principal principal) {
        TimesheetDTO result = timesheetService.submitTimesheet(dto, principal.getName());
        return ResponseEntity.ok(result);
    }

    // Employee: View own timesheets
    @GetMapping("/my")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<TimesheetDTO>> getOwnTimesheets(Principal principal) {
        List<TimesheetDTO> timesheets = timesheetService.getOwnTimesheets(principal.getName());
        return ResponseEntity.ok(timesheets);
    }

    // Admin: View all timesheets
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<TimesheetDTO>> getAllTimesheets() {
        List<TimesheetDTO> timesheets = timesheetService.getAllTimesheets();
        return ResponseEntity.ok(timesheets);
    }

    // Admin: Approve timesheet
    @PutMapping("/approve/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<TimesheetDTO> approveTimesheet(@PathVariable Long id) {
        TimesheetDTO result = timesheetService.approveTimesheet(id);
        return ResponseEntity.ok(result);
    }

    // Admin: Reject timesheet
    @PutMapping("/reject/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<TimesheetDTO> rejectTimesheet(@PathVariable Long id) {
        TimesheetDTO result = timesheetService.rejectTimesheet(id);
        return ResponseEntity.ok(result);
    }
} 
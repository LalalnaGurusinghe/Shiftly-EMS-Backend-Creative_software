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

    // User: Create timesheet
    @PostMapping("/add")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TimesheetDTO> createTimesheet(@RequestBody TimesheetDTO dto, Principal principal) {
        // Set userId from principal if needed
        return ResponseEntity.ok(timesheetService.createTimesheet(dto));
    }

    // SuperAdmin: Get all timesheets
    @GetMapping("/all")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<List<TimesheetDTO>> getAllTimesheets() {
        return ResponseEntity.ok(timesheetService.getAllTimesheets());
    }

    // User: Get timesheets by userId
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<TimesheetDTO>> getTimesheetsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(timesheetService.getTimesheetsByUserId(userId));
    }

    // User: Delete timesheet
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteTimesheet(@PathVariable Long id) {
        timesheetService.deleteTimesheet(id);
        return ResponseEntity.ok().build();
    }

    // SuperAdmin: Update timesheet status
    @PutMapping("/status/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<TimesheetDTO> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(timesheetService.updateStatus(id, status));
    }

    // User: Update timesheet
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TimesheetDTO> updateTimesheet(@PathVariable Long id, @RequestBody TimesheetDTO dto) {
        return ResponseEntity.ok(timesheetService.updateTimesheet(id, dto));
    }
}
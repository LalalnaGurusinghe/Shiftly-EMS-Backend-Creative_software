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
    
    private final TimesheetService timesheetService;

    public TimesheetController(TimesheetService timesheetService) {
        this.timesheetService = timesheetService;
    }

    @PostMapping("/add/{employeeId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TimesheetDTO> create(@PathVariable int employeeId,@RequestBody TimesheetDTO dto) {
        return ResponseEntity.ok(timesheetService.create(employeeId,dto));
    }

    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<TimesheetDTO>> getByEmployeeId(@PathVariable int employeeId) {
        return ResponseEntity.ok(timesheetService.getByEmployeeId(employeeId));
    }

    @GetMapping("/admin/{adminUserId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TimesheetDTO>> getTimesheetsForAdmin(@PathVariable Long adminUserId) {
        return ResponseEntity.ok(timesheetService.getTimesheetsForAdmin(adminUserId));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TimesheetDTO>> getAllTimesheets() {
        return ResponseEntity.ok(timesheetService.getAllTimesheets());
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteTimesheet(@PathVariable Long id) {
        timesheetService.deleteTimesheet(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/status/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TimesheetDTO> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(timesheetService.updateStatus(id, status));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TimesheetDTO> updateTimesheet(@PathVariable Long id, @RequestBody TimesheetDTO dto) {
        return ResponseEntity.ok(timesheetService.updateTimesheet(id, dto));
    }
}
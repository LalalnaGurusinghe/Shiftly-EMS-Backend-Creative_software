package com.EMS.Employee.Management.System.controller;

import com.EMS.Employee.Management.System.dto.LeaveDTO;
import com.EMS.Employee.Management.System.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shiftly/ems/leaves")
public class LeaveController {
    @Autowired
    private LeaveService leaveService;

    @PostMapping("/add/{employeeId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<LeaveDTO> applyLeave(@PathVariable int employeeId,@RequestBody LeaveDTO dto) {
        return ResponseEntity.ok(leaveService.applyLeave(employeeId,dto));
    }

    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<LeaveDTO>> getByEmployeeId(@PathVariable int employeeId) {
        return ResponseEntity.ok(leaveService.getByEmployeeId(employeeId));
    }

     @GetMapping("/admin/{adminUserId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<LeaveDTO>> getLeavesForAdmin(@PathVariable Long adminUserId) {
        return ResponseEntity.ok(leaveService.getLeavesForAdmin(adminUserId));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<LeaveDTO>> getAllLeaves() {
        return ResponseEntity.ok(leaveService.getAllLeaves());
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteLeave(@PathVariable Long id) {
        leaveService.deleteLeave(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/status/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<LeaveDTO> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(leaveService.updateStatus(id, status));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<LeaveDTO> updateLeave(@PathVariable Long id, @RequestBody LeaveDTO dto) {
        return ResponseEntity.ok(leaveService.updateLeave(id, dto));
    }

} 
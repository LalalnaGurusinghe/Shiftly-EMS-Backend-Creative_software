package com.EMS.Employee.Management.System.controller;

import com.EMS.Employee.Management.System.dto.CheckLeaveDTO;
import com.EMS.Employee.Management.System.entity.LeaveStatus;
import com.EMS.Employee.Management.System.service.CheckLeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shiftly/ems/admin/check-leave")
@CrossOrigin
public class CheckLeaveController {

    private final CheckLeaveService checkLeaveService;

    @Autowired
    public CheckLeaveController(CheckLeaveService checkLeaveService) {
        this.checkLeaveService = checkLeaveService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<CheckLeaveDTO>> getAllLeaveChecks() {
        List<CheckLeaveDTO> checkLeaveDTOList = checkLeaveService.getAll();
        return ResponseEntity.ok(checkLeaveDTOList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CheckLeaveDTO> getLeaveById(@PathVariable int id) {
        return checkLeaveService.getLeaveById(id);
    }

    @PutMapping("/update-status/{id}")
    public ResponseEntity<CheckLeaveDTO> updateLeaveStatus(
            @PathVariable int id,
            @RequestParam LeaveStatus newStatus) {
        return checkLeaveService.updateLeaveStatus(id, newStatus);
    }
}

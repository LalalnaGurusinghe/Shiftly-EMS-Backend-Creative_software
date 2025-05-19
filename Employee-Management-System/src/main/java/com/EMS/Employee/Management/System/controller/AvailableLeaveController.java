package com.EMS.Employee.Management.System.controller;

import com.EMS.Employee.Management.System.dto.AvailableLeaveDTO;
import com.EMS.Employee.Management.System.service.AvailableLeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaves")
@CrossOrigin(origins = "*") // Allow frontend access
public class AvailableLeaveController {

    @Autowired
    private AvailableLeaveService availableLeaveService;

    @GetMapping("/available")
    public List<AvailableLeaveDTO> getAvailableLeaves() {
        return availableLeaveService.getAllAvailableLeaves();
    }
}

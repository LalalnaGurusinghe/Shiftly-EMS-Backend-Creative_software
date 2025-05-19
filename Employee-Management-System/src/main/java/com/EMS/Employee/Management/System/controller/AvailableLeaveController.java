package com.EMS.Employee.Management.System.controller;

import com.EMS.Employee.Management.System.dto.AvailableLeaveDTO;
import com.EMS.Employee.Management.System.service.AvailableLeaveService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaves")
@CrossOrigin(origins = "*") // Allow frontend access
@NoArgsConstructor
public class AvailableLeaveController {

    private AvailableLeaveService availableLeaveService;

    public AvailableLeaveController(AvailableLeaveService availableLeaveService) {
        this.availableLeaveService = availableLeaveService;
    }

    @GetMapping("/available")
    public List<AvailableLeaveDTO> getAvailableLeaves() {
        return availableLeaveService.getAllAvailableLeaves();
    }
}

package com.EMS.Employee.Management.System.controller;

import com.EMS.Employee.Management.System.dto.RequestLeaveDTO;
import com.EMS.Employee.Management.System.service.RequestLeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shiftly/ems/leave")
@CrossOrigin
public class RequestLeaveController {

    @Autowired
    private RequestLeaveService requestLeaveService;


    @PostMapping("/add")
    public ResponseEntity<RequestLeaveDTO> addLeave(@RequestBody RequestLeaveDTO requestLeaveDTO) {
        return requestLeaveService.addLeave(requestLeaveDTO);
    }


    @GetMapping("/all")
    public List<RequestLeaveDTO> getAllLeaves() {
        return requestLeaveService.getAllLeaves();
    }


    @GetMapping("/{id}")
    public ResponseEntity<RequestLeaveDTO> getLeaveById(@PathVariable int id) {
        return requestLeaveService.getLeaveById(id);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<RequestLeaveDTO> deleteLeaveById(@PathVariable int id) {
        return requestLeaveService.deleteLeaveById(id);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<RequestLeaveDTO> updateLeaveById(@PathVariable int id, @RequestBody RequestLeaveDTO requestLeaveDTO) {
        return requestLeaveService.updateLeaveById(requestLeaveDTO, id);
    }
}

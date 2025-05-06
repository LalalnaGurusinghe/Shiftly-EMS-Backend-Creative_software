package com.EMS.Employee.Management.System.controller;

import com.EMS.Employee.Management.System.dto.RequestLeaveDTO;
import com.EMS.Employee.Management.System.service.RequestLeaveService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shiftly/ems/leave")
@CrossOrigin(origins = "http://localhost:3000")
public class RequestLeaveController {
    private final RequestLeaveService requestLeaveService;

    public RequestLeaveController(RequestLeaveService requestLeaveService) {
        this.requestLeaveService = requestLeaveService;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<RequestLeaveDTO> addLeave(@Valid @RequestBody RequestLeaveDTO requestLeaveDTO) {
        return requestLeaveService.addLeave(requestLeaveDTO);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<RequestLeaveDTO>> getAllLeaves() {
        return ResponseEntity.ok(requestLeaveService.getAllLeaves());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<RequestLeaveDTO> getLeaveById(@PathVariable int id) {
        return requestLeaveService.getLeaveById(id);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<RequestLeaveDTO> deleteLeaveById(@PathVariable int id) {
        return requestLeaveService.deleteLeaveById(id);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<RequestLeaveDTO> updateLeaveById(@PathVariable int id, @Valid @RequestBody RequestLeaveDTO requestLeaveDTO) {
        return requestLeaveService.updateLeaveById(requestLeaveDTO, id);
    }
}
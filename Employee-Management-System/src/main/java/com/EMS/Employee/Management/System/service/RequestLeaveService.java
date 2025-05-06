package com.EMS.Employee.Management.System.service;

import com.EMS.Employee.Management.System.dto.RequestLeaveDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RequestLeaveService {
    List<RequestLeaveDTO> getAllLeaves();
    ResponseEntity<RequestLeaveDTO> addLeave(RequestLeaveDTO requestLeaveDTO);
    ResponseEntity<RequestLeaveDTO> deleteLeaveById(int id);
    ResponseEntity<RequestLeaveDTO> updateLeaveById(RequestLeaveDTO requestLeaveDTO, int id);
    ResponseEntity<RequestLeaveDTO> getLeaveById(int id);
}
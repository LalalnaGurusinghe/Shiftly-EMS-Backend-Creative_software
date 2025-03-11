package com.EMS.Employee.Management.System.service;

import com.EMS.Employee.Management.System.dto.RequestLeaveDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface RequestLeaveService {

    public List<RequestLeaveDTO> getAllLeaves();

    public ResponseEntity<RequestLeaveDTO> addLeave(RequestLeaveDTO requestLeaveDTO);

    public ResponseEntity<RequestLeaveDTO> deleteLeaveById(int id);

    public ResponseEntity<RequestLeaveDTO> updateLeaveById(RequestLeaveDTO requestLeaveDTO , int id);

    public ResponseEntity<RequestLeaveDTO> getLeaveById(int id);
}

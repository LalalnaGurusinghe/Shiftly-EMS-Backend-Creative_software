package com.EMS.Employee.Management.System.service.impl;

import com.EMS.Employee.Management.System.dto.RequestLeaveDTO;
import com.EMS.Employee.Management.System.entity.CheckLeaveEntity;
import com.EMS.Employee.Management.System.entity.LeaveStatus;
import com.EMS.Employee.Management.System.entity.RequestLeaveEntity;
import com.EMS.Employee.Management.System.repo.CheckLeaveRepo;
import com.EMS.Employee.Management.System.repo.RequestLeaveRepo;
import com.EMS.Employee.Management.System.service.RequestLeaveService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RequestLeaveServiceImpl implements RequestLeaveService {

    private final RequestLeaveRepo requestLeaveRepo;
    private final CheckLeaveRepo checkLeaveRepo;

    public RequestLeaveServiceImpl(RequestLeaveRepo requestLeaveRepo, CheckLeaveRepo checkLeaveRepo) {
        this.requestLeaveRepo = requestLeaveRepo;
        this.checkLeaveRepo = checkLeaveRepo;
    }

    @Override
    public List<RequestLeaveDTO> getAllLeaves() {

        List<RequestLeaveEntity> requestLeaveEntities = requestLeaveRepo.findAll();

        List<RequestLeaveDTO> requestLeaveDTOs = requestLeaveEntities
                .stream().map(requestLeaveEntity -> new RequestLeaveDTO(
                        requestLeaveEntity.getLeaveId(),
                        requestLeaveEntity.getUserId(),
                        requestLeaveEntity.getUserName(),
                        requestLeaveEntity.getLeaveType(),
                        requestLeaveEntity.getLeaveFrom(),
                        requestLeaveEntity.getLeaveTo(),
                        requestLeaveEntity.getDuration(),
                        requestLeaveEntity.getCoverPerson(),
                        requestLeaveEntity.getReportTo(),
                        requestLeaveEntity.getReason(),
                        requestLeaveEntity.getStatus()
                ))
                .collect(Collectors.toList());

        return requestLeaveDTOs;
    }

    @Override
    public ResponseEntity<RequestLeaveDTO> addLeave(RequestLeaveDTO requestLeaveDTO) {
        RequestLeaveEntity requestLeaveEntity = new RequestLeaveEntity();

        BeanUtils.copyProperties(requestLeaveDTO, requestLeaveEntity);

        RequestLeaveEntity savedEntity = requestLeaveRepo.save(requestLeaveEntity);

        // Create CheckLeaveEntity and link it to the saved RequestLeaveEntity
        CheckLeaveEntity checkLeaveEntity = new CheckLeaveEntity();
        checkLeaveEntity.setRequestLeave(savedEntity);
        checkLeaveEntity.setStatus(LeaveStatus.PENDING);  // Set initial status to PENDING
        checkLeaveEntity.setAdminId(0);  // Set a default adminId, adjust as needed
        checkLeaveEntity.setAdminName("Admin");  // Set a default adminName, adjust as needed

        // Save CheckLeaveEntity
        checkLeaveRepo.save(checkLeaveEntity);

        RequestLeaveDTO savedDTO = new RequestLeaveDTO();
        BeanUtils.copyProperties(savedEntity, savedDTO);

        return ResponseEntity.ok(savedDTO);
    }

    @Override
    public ResponseEntity<RequestLeaveDTO> deleteLeaveById(int id) {

        Optional<RequestLeaveEntity> requestLeave = requestLeaveRepo.findById(id);

        if (requestLeave.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else {
            RequestLeaveDTO requestLeaveDTO = new RequestLeaveDTO();
            BeanUtils.copyProperties(requestLeave.get(), requestLeaveDTO);
            requestLeaveRepo.deleteById(id);
            return ResponseEntity.ok(requestLeaveDTO);
        }
    }

    @Override
    public ResponseEntity<RequestLeaveDTO> updateLeaveById(RequestLeaveDTO requestLeaveDTO, int id) {

        Optional<RequestLeaveEntity> leave = requestLeaveRepo.findById(id);

        if (leave.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        RequestLeaveEntity requestLeave = leave.get();

        // Updating the RequestLeaveEntity fields based on the incoming DTO values
        if (requestLeaveDTO.getUserId() != 0) {
            requestLeave.setUserId(requestLeaveDTO.getUserId());
        }
        if (requestLeaveDTO.getUserName() != null && !requestLeaveDTO.getUserName().isEmpty()) {
            requestLeave.setUserName(requestLeaveDTO.getUserName());
        }
        if (requestLeaveDTO.getLeaveType() != null && !requestLeaveDTO.getLeaveType().isEmpty()) {
            requestLeave.setLeaveType(requestLeaveDTO.getLeaveType());
        }
        if (requestLeaveDTO.getLeaveFrom() != null && !requestLeaveDTO.getLeaveFrom().isEmpty()) {
            requestLeave.setLeaveFrom(requestLeaveDTO.getLeaveFrom());
        }
        if (requestLeaveDTO.getLeaveTo() != null && !requestLeaveDTO.getLeaveTo().isEmpty()) {
            requestLeave.setLeaveTo(requestLeaveDTO.getLeaveTo());
        }
        if (requestLeaveDTO.getDuration() != 0) {
            requestLeave.setDuration(requestLeaveDTO.getDuration());
        }
        if (requestLeaveDTO.getCoverPerson() != null && !requestLeaveDTO.getCoverPerson().isEmpty()) {
            requestLeave.setCoverPerson(requestLeaveDTO.getCoverPerson());
        }
        if (requestLeaveDTO.getReportTo() != null && !requestLeaveDTO.getReportTo().isEmpty()) {
            requestLeave.setReportTo(requestLeaveDTO.getReportTo());
        }
        if (requestLeaveDTO.getReason() != null && !requestLeaveDTO.getReason().isEmpty()) {
            requestLeave.setReason(requestLeaveDTO.getReason());
        }
        if (requestLeaveDTO.getStatus() != null) {
            requestLeave.setStatus(requestLeaveDTO.getStatus());
        }

        // Save the updated leave entity
        requestLeaveRepo.save(requestLeave);

        BeanUtils.copyProperties(requestLeave, requestLeaveDTO);

        return ResponseEntity.ok(requestLeaveDTO);
    }

    @Override
    public ResponseEntity<RequestLeaveDTO> getLeaveById(int id) {

        Optional<RequestLeaveEntity> requestLeave = requestLeaveRepo.findById(id);

        if (requestLeave.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        RequestLeaveDTO requestLeaveDTO = new RequestLeaveDTO();
        BeanUtils.copyProperties(requestLeave.get(), requestLeaveDTO);

        return ResponseEntity.ok(requestLeaveDTO);
    }
}

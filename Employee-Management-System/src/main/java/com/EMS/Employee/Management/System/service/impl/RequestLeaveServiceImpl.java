package com.EMS.Employee.Management.System.service.impl;

import com.EMS.Employee.Management.System.dto.RequestLeaveDTO;
import com.EMS.Employee.Management.System.entity.CheckLeaveEntity;
import com.EMS.Employee.Management.System.entity.LeaveStatus;
import com.EMS.Employee.Management.System.entity.RequestLeaveEntity;
import com.EMS.Employee.Management.System.repo.CheckLeaveRepo;
import com.EMS.Employee.Management.System.repo.RequestLeaveRepo;
import com.EMS.Employee.Management.System.service.RequestLeaveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RequestLeaveServiceImpl implements RequestLeaveService {
    private static final Logger logger = LoggerFactory.getLogger(RequestLeaveServiceImpl.class);
    private final RequestLeaveRepo requestLeaveRepo;
    private final CheckLeaveRepo checkLeaveRepo;

    public RequestLeaveServiceImpl(RequestLeaveRepo requestLeaveRepo, CheckLeaveRepo checkLeaveRepo) {
        this.requestLeaveRepo = requestLeaveRepo;
        this.checkLeaveRepo = checkLeaveRepo;
    }

    @Override
    public List<RequestLeaveDTO> getAllLeaves() {
        return requestLeaveRepo.findAll().stream()
                .map(requestLeaveEntity -> {
                    RequestLeaveDTO requestLeaveDTO = new RequestLeaveDTO();
                    BeanUtils.copyProperties(requestLeaveEntity, requestLeaveDTO);
                    return requestLeaveDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<RequestLeaveDTO> addLeave(RequestLeaveDTO requestLeaveDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        requestLeaveDTO.setUserName(username);
        RequestLeaveEntity requestLeaveEntity = new RequestLeaveEntity();
        BeanUtils.copyProperties(requestLeaveDTO, requestLeaveEntity);
        RequestLeaveEntity savedEntity = requestLeaveRepo.save(requestLeaveEntity);
        CheckLeaveEntity checkLeaveEntity = new CheckLeaveEntity();
        checkLeaveEntity.setRequestLeave(savedEntity);
        checkLeaveEntity.setStatus(LeaveStatus.PENDING);
        checkLeaveEntity.setAdminId(null); // Use null until an admin processes
        checkLeaveEntity.setAdminName(null); // Use null until an admin processes
        checkLeaveRepo.save(checkLeaveEntity);
        RequestLeaveDTO savedDTO = new RequestLeaveDTO();
        BeanUtils.copyProperties(savedEntity, savedDTO);
        logger.info("Leave request added for user: {}", username);
        return ResponseEntity.ok(savedDTO);
    }

    @Override
    public ResponseEntity<RequestLeaveDTO> deleteLeaveById(int id) {
        Optional<RequestLeaveEntity> requestLeave = requestLeaveRepo.findById(id);
        if (requestLeave.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        RequestLeaveDTO requestLeaveDTO = new RequestLeaveDTO();
        BeanUtils.copyProperties(requestLeave.get(), requestLeaveDTO);
        requestLeaveRepo.deleteById(id);
        logger.info("Leave request deleted with ID: {}", id);
        return ResponseEntity.ok(requestLeaveDTO);
    }

    @Override
    public ResponseEntity<RequestLeaveDTO> updateLeaveById(RequestLeaveDTO requestLeaveDTO, int id) {
        Optional<RequestLeaveEntity> leave = requestLeaveRepo.findById(id);
        if (leave.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        RequestLeaveEntity requestLeave = leave.get();
        if (requestLeaveDTO.getUserId() != null) requestLeave.setUserId(requestLeaveDTO.getUserId());
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
        if (requestLeaveDTO.getDuration() != 0) requestLeave.setDuration(requestLeaveDTO.getDuration());
        if (requestLeaveDTO.getCoverPerson() != null && !requestLeaveDTO.getCoverPerson().isEmpty()) {
            requestLeave.setCoverPerson(requestLeaveDTO.getCoverPerson());
        }
        if (requestLeaveDTO.getReportTo() != null && !requestLeaveDTO.getReportTo().isEmpty()) {
            requestLeave.setReportTo(requestLeaveDTO.getReportTo());
        }
        if (requestLeaveDTO.getReason() != null && !requestLeaveDTO.getReason().isEmpty()) {
            requestLeave.setReason(requestLeaveDTO.getReason());
        }
        if (requestLeaveDTO.getStatus() != null) requestLeave.setStatus(requestLeaveDTO.getStatus());
        requestLeaveRepo.save(requestLeave);
        BeanUtils.copyProperties(requestLeave, requestLeaveDTO);
        logger.info("Leave request updated with ID: {}", id);
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
package com.EMS.Employee.Management.System.service.impl;

import com.EMS.Employee.Management.System.dto.RequestLeaveDTO;
import com.EMS.Employee.Management.System.entity.CheckLeaveEntity;
import com.EMS.Employee.Management.System.entity.LeaveStatus;
import com.EMS.Employee.Management.System.entity.RequestLeaveEntity;
import com.EMS.Employee.Management.System.entity.User;
import com.EMS.Employee.Management.System.repo.CheckLeaveRepo;
import com.EMS.Employee.Management.System.repo.RequestLeaveRepo;
import com.EMS.Employee.Management.System.repo.UserRepo;
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
    private final UserRepo userRepo;

    public RequestLeaveServiceImpl(RequestLeaveRepo requestLeaveRepo, CheckLeaveRepo checkLeaveRepo, UserRepo userRepo) {
        this.requestLeaveRepo = requestLeaveRepo;
        this.checkLeaveRepo = checkLeaveRepo;
        this.userRepo = userRepo;
    }

    @Override
    public List<RequestLeaveDTO> getAllLeaves() {
        return requestLeaveRepo.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<RequestLeaveDTO> addLeave(RequestLeaveDTO requestLeaveDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        // Business rule: Prevent overlapping leaves for the same user
        boolean overlap = requestLeaveRepo.findAll().stream().anyMatch(existing ->
            existing.getUser().getId().equals(user.getId()) &&
            !(existing.getLeaveTo().isBefore(requestLeaveDTO.getLeaveFrom()) ||
              existing.getLeaveFrom().isAfter(requestLeaveDTO.getLeaveTo()))
        );
        if (overlap) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
        RequestLeaveEntity requestLeaveEntity = convertToEntity(requestLeaveDTO, user, username);
        RequestLeaveEntity savedEntity = requestLeaveRepo.save(requestLeaveEntity);
        CheckLeaveEntity checkLeaveEntity = new CheckLeaveEntity();
        checkLeaveEntity.setRequestLeave(savedEntity);
        checkLeaveEntity.setStatus(LeaveStatus.PENDING);
        checkLeaveEntity.setAdminId(null);
        checkLeaveEntity.setAdminName(null);
        checkLeaveRepo.save(checkLeaveEntity);
        RequestLeaveDTO savedDTO = convertToDTO(savedEntity);
        logger.info("Leave request added for user: {}", username);
        return ResponseEntity.ok(savedDTO);
    }

    @Override
    public ResponseEntity<RequestLeaveDTO> deleteLeaveById(int id) {
        Optional<RequestLeaveEntity> requestLeave = requestLeaveRepo.findById(id);
        if (requestLeave.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        RequestLeaveDTO requestLeaveDTO = convertToDTO(requestLeave.get());
        requestLeaveRepo.deleteById(id);
        logger.info("Leave request deleted with ID: {}", id);
        return ResponseEntity.ok(requestLeaveDTO);
    }

    @Override
    public ResponseEntity<RequestLeaveDTO> updateLeaveById(RequestLeaveDTO requestLeaveDTO, int id) {
        Optional<RequestLeaveEntity> leaveOpt = requestLeaveRepo.findById(id);
        if (leaveOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        RequestLeaveEntity leave = leaveOpt.get();
        // Only update allowed fields
        if (requestLeaveDTO.getLeaveType() != null) leave.setLeaveType(requestLeaveDTO.getLeaveType());
        if (requestLeaveDTO.getLeaveFrom() != null) leave.setLeaveFrom(requestLeaveDTO.getLeaveFrom());
        if (requestLeaveDTO.getLeaveTo() != null) leave.setLeaveTo(requestLeaveDTO.getLeaveTo());
        if (requestLeaveDTO.getDuration() != 0) leave.setDuration(requestLeaveDTO.getDuration());
        if (requestLeaveDTO.getCoverPerson() != null) leave.setCoverPerson(requestLeaveDTO.getCoverPerson());
        if (requestLeaveDTO.getReportTo() != null) leave.setReportTo(requestLeaveDTO.getReportTo());
        if (requestLeaveDTO.getReason() != null) leave.setReason(requestLeaveDTO.getReason());
        if (requestLeaveDTO.getStatus() != null) leave.setStatus(requestLeaveDTO.getStatus());
        leave.setUpdatedBy(SecurityContextHolder.getContext().getAuthentication().getName());
        requestLeaveRepo.save(leave);
        RequestLeaveDTO updatedDTO = convertToDTO(leave);
        logger.info("Leave request updated with ID: {}", id);
        return ResponseEntity.ok(updatedDTO);
    }

    @Override
    public ResponseEntity<RequestLeaveDTO> getLeaveById(int id) {
        Optional<RequestLeaveEntity> requestLeave = requestLeaveRepo.findById(id);
        if (requestLeave.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        RequestLeaveDTO requestLeaveDTO = convertToDTO(requestLeave.get());
        return ResponseEntity.ok(requestLeaveDTO);
    }

    private RequestLeaveDTO convertToDTO(RequestLeaveEntity entity) {
        RequestLeaveDTO dto = new RequestLeaveDTO();
        dto.setLeaveId(entity.getLeaveId());
        dto.setUserId(entity.getUser() != null ? entity.getUser().getId() : null);
        dto.setLeaveType(entity.getLeaveType());
        dto.setLeaveFrom(entity.getLeaveFrom());
        dto.setLeaveTo(entity.getLeaveTo());
        dto.setDuration(entity.getDuration());
        dto.setCoverPerson(entity.getCoverPerson());
        dto.setReportTo(entity.getReportTo());
        dto.setReason(entity.getReason());
        dto.setStatus(entity.getStatus());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setUpdatedBy(entity.getUpdatedBy());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }

    private RequestLeaveEntity convertToEntity(RequestLeaveDTO dto, User user, String createdBy) {
        RequestLeaveEntity entity = new RequestLeaveEntity();
        entity.setUser(user);
        entity.setLeaveType(dto.getLeaveType());
        entity.setLeaveFrom(dto.getLeaveFrom());
        entity.setLeaveTo(dto.getLeaveTo());
        entity.setDuration(dto.getDuration());
        entity.setCoverPerson(dto.getCoverPerson());
        entity.setReportTo(dto.getReportTo());
        entity.setReason(dto.getReason());
        entity.setStatus(dto.getStatus() != null ? dto.getStatus() : LeaveStatus.PENDING);
        entity.setCreatedBy(createdBy);
        entity.setUpdatedBy(createdBy);
        return entity;
    }
}
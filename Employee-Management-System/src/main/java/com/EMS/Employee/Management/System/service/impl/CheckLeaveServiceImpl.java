package com.EMS.Employee.Management.System.service.impl;

import com.EMS.Employee.Management.System.dto.CheckLeaveDTO;
import com.EMS.Employee.Management.System.dto.RequestLeaveDTO;
import com.EMS.Employee.Management.System.entity.CheckLeaveEntity;
import com.EMS.Employee.Management.System.entity.LeaveStatus;
import com.EMS.Employee.Management.System.entity.RequestLeaveEntity;
import com.EMS.Employee.Management.System.repo.CheckLeaveRepo;
import com.EMS.Employee.Management.System.repo.RequestLeaveRepo;
import com.EMS.Employee.Management.System.service.CheckLeaveService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CheckLeaveServiceImpl implements CheckLeaveService {

    private final CheckLeaveRepo checkLeaveRepo;
    private final RequestLeaveRepo requestLeaveRepo;

    public CheckLeaveServiceImpl(CheckLeaveRepo checkLeaveRepo, RequestLeaveRepo requestLeaveRepo) {
        this.checkLeaveRepo = checkLeaveRepo;
        this.requestLeaveRepo = requestLeaveRepo;
    }

    @Override
    public List<CheckLeaveDTO> getAll() {
        List<CheckLeaveEntity> checkLeaveEntities = checkLeaveRepo.findAll();
        return checkLeaveEntities.stream()
                .map(checkLeaveEntity -> {
                    // Create CheckLeaveDTO from CheckLeaveEntity
                    CheckLeaveDTO checkLeaveDTO = new CheckLeaveDTO();
                    BeanUtils.copyProperties(checkLeaveEntity, checkLeaveDTO);

                    // Get the RequestLeaveEntity and map it to RequestLeaveDTO
                    RequestLeaveEntity requestLeave = checkLeaveEntity.getRequestLeave();
                    RequestLeaveDTO requestLeaveDTO = new RequestLeaveDTO();
                    BeanUtils.copyProperties(requestLeave, requestLeaveDTO);

                    // Set the RequestLeaveDTO in CheckLeaveDTO
                    checkLeaveDTO.setRequestLeaveDTO(requestLeaveDTO);
                    return checkLeaveDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<CheckLeaveDTO> getLeaveById(int id) {
        Optional<CheckLeaveEntity> checkLeave = checkLeaveRepo.findById(id);
        if (checkLeave.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CheckLeaveDTO()); // return empty or error response
        }

        CheckLeaveEntity checkLeaveEntity = checkLeave.get();
        CheckLeaveDTO checkLeaveDTO = new CheckLeaveDTO();
        BeanUtils.copyProperties(checkLeaveEntity, checkLeaveDTO);

        // Map RequestLeaveEntity to RequestLeaveDTO
        RequestLeaveEntity requestLeave = checkLeaveEntity.getRequestLeave();
        RequestLeaveDTO requestLeaveDTO = new RequestLeaveDTO();
        BeanUtils.copyProperties(requestLeave, requestLeaveDTO);

        // Set RequestLeaveDTO in CheckLeaveDTO
        checkLeaveDTO.setRequestLeaveDTO(requestLeaveDTO);

        return ResponseEntity.ok(checkLeaveDTO);
    }

    @Override
    public ResponseEntity<CheckLeaveDTO> updateLeaveStatus(int id, LeaveStatus leaveStatus) {
        Optional<CheckLeaveEntity> checkLeave = checkLeaveRepo.findById(id);
        if (checkLeave.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new CheckLeaveDTO()); // return empty or error response
        }

        CheckLeaveEntity checkLeaveEntity = checkLeave.get();
        checkLeaveEntity.setStatus(leaveStatus);

        // Update the corresponding RequestLeaveEntity status
        RequestLeaveEntity requestLeave = checkLeaveEntity.getRequestLeave();
        requestLeave.setStatus(leaveStatus);  // Update RequestLeave status

        requestLeaveRepo.save(requestLeave);  // Save updated RequestLeaveEntity
        checkLeaveRepo.save(checkLeaveEntity);  // Save updated CheckLeaveEntity

        CheckLeaveDTO checkLeaveDTO = new CheckLeaveDTO();
        BeanUtils.copyProperties(checkLeaveEntity, checkLeaveDTO);

        // Map RequestLeaveEntity to RequestLeaveDTO
        RequestLeaveDTO requestLeaveDTO = new RequestLeaveDTO();
        BeanUtils.copyProperties(requestLeave, requestLeaveDTO);

        // Set RequestLeaveDTO in CheckLeaveDTO
        checkLeaveDTO.setRequestLeaveDTO(requestLeaveDTO);

        return ResponseEntity.ok(checkLeaveDTO);
    }
}

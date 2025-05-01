package com.EMS.Employee.Management.System.service.impl;

import com.EMS.Employee.Management.System.dto.CheckLeaveDTO;
import com.EMS.Employee.Management.System.dto.RequestLeaveDTO;
import com.EMS.Employee.Management.System.entity.CheckLeaveEntity;
import com.EMS.Employee.Management.System.entity.LeaveStatus;
import com.EMS.Employee.Management.System.entity.RequestLeaveEntity;
import com.EMS.Employee.Management.System.entity.User;
import com.EMS.Employee.Management.System.repo.CheckLeaveRepo;
import com.EMS.Employee.Management.System.repo.RequestLeaveRepo;
import com.EMS.Employee.Management.System.repo.UserRepository;
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
    private final UserRepository userRepository;

    public CheckLeaveServiceImpl(CheckLeaveRepo checkLeaveRepo, RequestLeaveRepo requestLeaveRepo, UserRepository userRepository) {
        this.checkLeaveRepo = checkLeaveRepo;
        this.requestLeaveRepo = requestLeaveRepo;
        this.userRepository = userRepository;
    }

    @Override
    public List<CheckLeaveDTO> getAll() {
        List<CheckLeaveEntity> checkLeaveEntities = checkLeaveRepo.findAll();
        return checkLeaveEntities.stream()
                .map(checkLeaveEntity -> {
                    CheckLeaveDTO checkLeaveDTO = new CheckLeaveDTO();
                    BeanUtils.copyProperties(checkLeaveEntity, checkLeaveDTO);
                    RequestLeaveEntity requestLeave = checkLeaveEntity.getRequestLeave();
                    RequestLeaveDTO requestLeaveDTO = new RequestLeaveDTO();
                    BeanUtils.copyProperties(requestLeave, requestLeaveDTO);
                    checkLeaveDTO.setRequestLeaveDTO(requestLeaveDTO);
                    return checkLeaveDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<CheckLeaveDTO> getLeaveById(int id) {
        Optional<CheckLeaveEntity> checkLeave = checkLeaveRepo.findById(id);
        if (checkLeave.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        CheckLeaveEntity checkLeaveEntity = checkLeave.get();
        CheckLeaveDTO checkLeaveDTO = new CheckLeaveDTO();
        BeanUtils.copyProperties(checkLeaveEntity, checkLeaveDTO);
        RequestLeaveEntity requestLeave = checkLeaveEntity.getRequestLeave();
        RequestLeaveDTO requestLeaveDTO = new RequestLeaveDTO();
        BeanUtils.copyProperties(requestLeave, requestLeaveDTO);
        checkLeaveDTO.setRequestLeaveDTO(requestLeaveDTO);
        return ResponseEntity.ok(checkLeaveDTO);
    }

    @Override
    public ResponseEntity<CheckLeaveDTO> updateLeaveStatus(int id, LeaveStatus leaveStatus, String adminEmail) {
        Optional<CheckLeaveEntity> checkLeave = checkLeaveRepo.findById(id);
        if (checkLeave.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        User admin = userRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        CheckLeaveEntity checkLeaveEntity = checkLeave.get();
        checkLeaveEntity.setStatus(leaveStatus);
        checkLeaveEntity.setAdminId(admin.getId());
        checkLeaveEntity.setAdminName(admin.getFirstName() + " " + admin.getLastName());

        RequestLeaveEntity requestLeave = checkLeaveEntity.getRequestLeave();
        requestLeave.setStatus(leaveStatus);

        requestLeaveRepo.save(requestLeave);
        checkLeaveRepo.save(checkLeaveEntity);

        CheckLeaveDTO checkLeaveDTO = new CheckLeaveDTO();
        BeanUtils.copyProperties(checkLeaveEntity, checkLeaveDTO);
        RequestLeaveDTO requestLeaveDTO = new RequestLeaveDTO();
        BeanUtils.copyProperties(requestLeave, requestLeaveDTO);
        checkLeaveDTO.setRequestLeaveDTO(requestLeaveDTO);
        return ResponseEntity.ok(checkLeaveDTO);
    }
}
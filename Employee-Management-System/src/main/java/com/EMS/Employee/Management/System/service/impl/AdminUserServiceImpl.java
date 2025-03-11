package com.EMS.Employee.Management.System.service.impl;

import com.EMS.Employee.Management.System.dto.AdminUserDTO;
import com.EMS.Employee.Management.System.entity.AdminUserEntity;
import com.EMS.Employee.Management.System.repo.AdminUserRepo;
import com.EMS.Employee.Management.System.service.AdminUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminUserServiceImpl implements AdminUserService {

    private final AdminUserRepo adminUserRepo;

    public AdminUserServiceImpl(AdminUserRepo adminUserRepo) {
        this.adminUserRepo = adminUserRepo;
    }

    @Override
    public ResponseEntity<AdminUserDTO> addUser(AdminUserDTO adminUserDTO) {
        AdminUserEntity adminUserEntity = new AdminUserEntity();
        BeanUtils.copyProperties(adminUserDTO, adminUserEntity);
        AdminUserEntity savedEntity = adminUserRepo.save(adminUserEntity);
        adminUserDTO.setEmployeeNo(savedEntity.getEmployeeNo());
        return ResponseEntity.status(HttpStatus.CREATED).body(adminUserDTO);
    }

    //****valuable point
    @Override
    public List<AdminUserDTO> getAll() {
        return adminUserRepo.findAll()
                .stream()
                .map(entity -> {
                    AdminUserDTO dto = new AdminUserDTO();
                    BeanUtils.copyProperties(entity, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<AdminUserDTO> getUserById(int id) {
        AdminUserEntity entity = adminUserRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin user not found"));
        AdminUserDTO dto = new AdminUserDTO();
        BeanUtils.copyProperties(entity, dto);
        return ResponseEntity.ok(dto);
    }

    @Transactional
    @Override
    public ResponseEntity<AdminUserDTO> deleteUserById(int id) {
        Optional<AdminUserEntity> optionalAdminUser = adminUserRepo.findById(id);

        if(optionalAdminUser.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        AdminUserDTO adminUserDTO = new AdminUserDTO();

        BeanUtils.copyProperties(optionalAdminUser.get() , adminUserDTO);

        adminUserRepo.deleteById(id);

        return ResponseEntity.ok(adminUserDTO);
    }

    @Override
    public ResponseEntity<AdminUserDTO> updateUserById(int id, AdminUserDTO adminUserDTO) {
        AdminUserEntity entity = adminUserRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin user not found"));

        if (adminUserDTO.getFirstName() != null) entity.setFirstName(adminUserDTO.getFirstName());
        if (adminUserDTO.getLastName() != null) entity.setLastName(adminUserDTO.getLastName());
        if (adminUserDTO.getGender() != null) entity.setGender(adminUserDTO.getGender());
        if (adminUserDTO.getBirthOfDate() != null) entity.setBirthOfDate(adminUserDTO.getBirthOfDate());
        if (adminUserDTO.getLocation() != null) entity.setLocation(adminUserDTO.getLocation());
        if (adminUserDTO.getEmail() != null) entity.setEmail(adminUserDTO.getEmail());
        if (adminUserDTO.getEpfNO() != 0) entity.setEpfNO(adminUserDTO.getEpfNO());
        if (adminUserDTO.getDesignation() != null) entity.setDesignation(adminUserDTO.getDesignation());
        if (adminUserDTO.getDepartment() != null) entity.setDepartment(adminUserDTO.getDepartment());
        if (adminUserDTO.getReportingPerson() != null) entity.setReportingPerson(adminUserDTO.getReportingPerson());

        adminUserRepo.save(entity);
        AdminUserDTO updatedDTO = new AdminUserDTO();
        BeanUtils.copyProperties(entity, updatedDTO);

        return ResponseEntity.ok(updatedDTO);
    }
}

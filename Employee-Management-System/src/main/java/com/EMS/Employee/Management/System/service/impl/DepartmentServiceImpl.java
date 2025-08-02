package com.EMS.Employee.Management.System.service.impl;

import com.EMS.Employee.Management.System.dto.DepartmentDTO;
import com.EMS.Employee.Management.System.entity.DepartmentEntity;
import com.EMS.Employee.Management.System.repo.DepartmentRepo;
import com.EMS.Employee.Management.System.repo.UserRepo;
import com.EMS.Employee.Management.System.entity.User;
import com.EMS.Employee.Management.System.service.DepartmentService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepo departmentRepo;
    private final UserRepo userRepo;

    public DepartmentServiceImpl(DepartmentRepo departmentRepo, UserRepo userRepo) {
        this.departmentRepo = departmentRepo;
        this.userRepo = userRepo;
    }

    @Override
    public List<DepartmentDTO> getAllDepartments() {
        return departmentRepo.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public DepartmentDTO createDepartment(DepartmentDTO departmentDto) {
        DepartmentEntity entity = new DepartmentEntity();
        entity.setName(departmentDto.getDepartmentName());
        DepartmentEntity saved = departmentRepo.save(entity);

        return toDTO(saved);

    }

    @Override
    public DepartmentDTO assignAdmin(Long userId, Long departmentId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        DepartmentEntity departmentEntity = departmentRepo.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        departmentEntity.setAdminId(user.getId());
        departmentEntity.setName(departmentEntity.getName());
        departmentEntity.setId(departmentId);
        DepartmentEntity saved = departmentRepo.save(departmentEntity);

        return toDTO(saved);
    }

    @Override
    public String getDepartmentNameById(Long departmentId) {
        return departmentRepo.findById(departmentId).map(DepartmentEntity::getName).orElse(null);
    }

    private DepartmentDTO toDTO(DepartmentEntity entity) {
        DepartmentDTO dto = new DepartmentDTO();
        dto.setDepartmentId(entity.getId());
        dto.setDepartmentName(entity.getName());
        dto.setAdminId(entity.getAdminId());

        return dto;
    }
}

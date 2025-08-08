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
    public List<DepartmentDTO> getAllDepartmentsWithAdmin() {
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

        departmentEntity.setAdmin(user); // Set the admin as User object
        DepartmentEntity saved = departmentRepo.save(departmentEntity);

        return toDTO(saved);
    }

    @Override
    public void deleteDepartment(Long departmentId) {
        if (!departmentRepo.existsById(departmentId)) {
            throw new RuntimeException("Department not found");
        }
        departmentRepo.deleteById(departmentId);
    }

    @Override
    public DepartmentDTO getDepartmentIdByAdminUserId(Long adminUserId) {
        return departmentRepo.findAll().stream()
                .filter(dept -> dept.getAdmin() != null && dept.getAdmin().getId().equals(adminUserId))
                .findFirst()
                .map(this::toDTO)
                .orElse(null);
    }

    private DepartmentDTO toDTO(DepartmentEntity entity) {
        DepartmentDTO dto = new DepartmentDTO();
        dto.setDepartmentId(entity.getId());
        dto.setDepartmentName(entity.getName());
        if (entity.getAdmin() != null) {
            dto.setAdminId(entity.getAdmin().getId());
            dto.setAdminUserName(entity.getAdmin().getUsername());
        } else {
            dto.setAdminId(null);
            dto.setAdminUserName(null);
        }
        return dto;
    }
}

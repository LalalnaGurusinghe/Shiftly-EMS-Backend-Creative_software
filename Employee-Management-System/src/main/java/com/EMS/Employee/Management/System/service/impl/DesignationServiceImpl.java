package com.EMS.Employee.Management.System.service.impl;

import com.EMS.Employee.Management.System.dto.DesignationDTO;
import com.EMS.Employee.Management.System.entity.DepartmentEntity;
import com.EMS.Employee.Management.System.entity.DesignationEntity;
import com.EMS.Employee.Management.System.repo.DepartmentRepo;
import com.EMS.Employee.Management.System.repo.DesignationRepo;
import com.EMS.Employee.Management.System.service.DesignationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DesignationServiceImpl implements DesignationService {

    private final DesignationRepo designationRepo;
    private final DepartmentRepo departmentRepo;

    public DesignationServiceImpl(DesignationRepo designationRepo, DepartmentRepo departmentRepo) {
        this.designationRepo = designationRepo;
        this.departmentRepo = departmentRepo;
    }

    @Override
    @Transactional
    public DesignationDTO createDesignation(DesignationDTO dto) {
        DepartmentEntity department = departmentRepo.findById(dto.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));
        DesignationEntity entity = new DesignationEntity();
        entity.setName(dto.getDesignationName());
        entity.setDepartment(department);
        DesignationEntity saved = designationRepo.save(entity);
        return toDTO(saved);
    }

    @Override
    @Transactional
    public DesignationDTO updateDesignation(Long id, DesignationDTO dto) {
        DesignationEntity entity = designationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Designation not found"));
        entity.setName(dto.getDesignationName());
        if (dto.getDepartmentId() != null) {
            DepartmentEntity department = departmentRepo.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found"));
            entity.setDepartment(department);
        }
        DesignationEntity saved = designationRepo.save(entity);
        return toDTO(saved);
    }

    @Override
    @Transactional
    public void deleteDesignation(Long id) {
        if (!designationRepo.existsById(id)) {
            throw new RuntimeException("Designation not found");
        }
        designationRepo.deleteById(id);
    }

    @Override
    public List<DesignationDTO> getAllDesignations() {
        return designationRepo.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<DesignationDTO> getDesignationsByDepartmentId(Long departmentId) {
        return designationRepo.findAll().stream()
                .filter(designation -> designation.getDepartment() != null
                        && designation.getDepartment().getId().equals(departmentId))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // @Override
    // public DesignationDTO getDesignationById(Long id) {
    //     DesignationEntity entity = designationRepo.findById(id)
    //             .orElseThrow(() -> new RuntimeException("Designation not found"));
    //     return toDTO(entity);
    // }

    private DesignationDTO toDTO(DesignationEntity entity) {
        DesignationDTO dto = new DesignationDTO();
        dto.setId(entity.getId());
        dto.setDesignationName(entity.getName());
        dto.setDepartmentId(entity.getDepartment().getId());
        dto.setDepartmentName(entity.getDepartment().getName());
        return dto;
    }
}
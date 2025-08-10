package com.EMS.Employee.Management.System.service.impl;

import com.EMS.Employee.Management.System.dto.DesignationDTO;
import com.EMS.Employee.Management.System.dto.VacancyDTO;
import com.EMS.Employee.Management.System.entity.DepartmentEntity;
import com.EMS.Employee.Management.System.entity.DesignationEntity;
import com.EMS.Employee.Management.System.repo.DepartmentRepo;
import com.EMS.Employee.Management.System.entity.VacancyEntity;
import com.EMS.Employee.Management.System.repo.VacancyRepo;
import com.EMS.Employee.Management.System.service.VacancyService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VacancyServiceImpl implements VacancyService {
    private final VacancyRepo vacancyRepo;
    private final DepartmentRepo departmentRepo;

    public VacancyServiceImpl(VacancyRepo vacancyRepo,DepartmentRepo departmentRepo) {
        this.vacancyRepo = vacancyRepo;
        this.departmentRepo = departmentRepo;
    }

    @Override
    @Transactional
    public VacancyDTO createVacancy(VacancyDTO dto) {
        DepartmentEntity department = departmentRepo.findById(dto.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));
        VacancyEntity entity = new VacancyEntity();
        entity.setName(dto.getVacancyName());
        entity.setDepartment(department);
        VacancyEntity saved = vacancyRepo.save(entity);
        return toDTO(saved);
    }

    @Override
    @Transactional
    public VacancyDTO updateVacancy(Long id, VacancyDTO dto) {
        VacancyEntity entity = vacancyRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Vacancy not found"));
        entity.setName(dto.getVacancyName());
        if (dto.getDepartmentId() != null) {
            DepartmentEntity department = departmentRepo.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found"));
            entity.setDepartment(department);
        }
        VacancyEntity saved = vacancyRepo.save(entity);
        return toDTO(saved);
    }

    @Override
    @Transactional
    public void deleteVacancy(Long id) {
        if (!vacancyRepo.existsById(id)) {
            throw new RuntimeException("Vacancy not found");
        }
        vacancyRepo.deleteById(id);
    }

    @Override
    public List<VacancyDTO> getAllVacancies() {
        return vacancyRepo.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<VacancyDTO> getVacancyByDepartmentId(Long departmentId) {
        return vacancyRepo.findAll().stream()
                .filter(vacancy -> vacancy.getDepartment() != null
                        && vacancy.getDepartment().getId().equals(departmentId))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // @Override
    // public VacancyDTO getVacancyById(Long id) {
    //     Optional<VacancyEntity> entity = vacancyRepo.findById(id);
    //     if (entity.isEmpty()) return null;
    //     VacancyDTO dto = new VacancyDTO();
    //     BeanUtils.copyProperties(entity.get(), dto);
    //     return dto;
    // }

    private VacancyDTO toDTO(VacancyEntity entity) {
        VacancyDTO dto = new VacancyDTO();
        dto.setId(entity.getId());
        dto.setVacancyName(entity.getName());
        dto.setDepartmentId(entity.getDepartment().getId());
        dto.setDepartmentName(entity.getDepartment().getName());
        return dto;
    }


} 
package com.EMS.Employee.Management.System.service.impl;

import com.EMS.Employee.Management.System.dto.EducationDTO;
import com.EMS.Employee.Management.System.entity.EducationEntity;
import com.EMS.Employee.Management.System.entity.EmployeeEntity;
import com.EMS.Employee.Management.System.repo.EducationRepo;
import com.EMS.Employee.Management.System.repo.EmployeeRepo;
import com.EMS.Employee.Management.System.service.EducationService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EducationServiceImpl implements EducationService {
    private final EducationRepo educationRepo;
    private final EmployeeRepo employeeRepo;

    public EducationServiceImpl(EducationRepo educationRepo, EmployeeRepo employeeRepo) {
        this.educationRepo = educationRepo;
        this.employeeRepo = employeeRepo;
    }

    @Override
    public EducationDTO addEducation(EducationDTO educationDTO) {
        EducationEntity educationEntity = new EducationEntity();
        BeanUtils.copyProperties(educationDTO, educationEntity);
        if (educationDTO.getEmployeeId() != null) {
            EmployeeEntity employee = employeeRepo.findById(educationDTO.getEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Employee not found"));
            educationEntity.setEmployee(employee);
        }
        EducationEntity saved = educationRepo.save(educationEntity);
        EducationDTO dto = new EducationDTO();
        BeanUtils.copyProperties(saved, dto);
        dto.setEmployeeId(saved.getEmployee() != null ? saved.getEmployee().getEmployeeId() : null);
        return dto;
    }

    @Override
    public EducationDTO updateEducation(Long id, EducationDTO educationDTO) {
        EducationEntity educationEntity = educationRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Education not found"));
        if (educationDTO.getName() != null) educationEntity.setName(educationDTO.getName());
        EducationEntity saved = educationRepo.save(educationEntity);
        EducationDTO dto = new EducationDTO();
        BeanUtils.copyProperties(saved, dto);
        dto.setEmployeeId(saved.getEmployee() != null ? saved.getEmployee().getEmployeeId() : null);
        return dto;
    }

    @Override
    public void deleteEducation(Long id) {
        educationRepo.deleteById(id);
    }

    @Override
    public List<EducationDTO> getEducationByEmployeeId(Integer employeeId) {
        return educationRepo.findByEmployee_EmployeeId(employeeId)
                .stream()
                .map(education -> {
                    EducationDTO dto = new EducationDTO();
                    BeanUtils.copyProperties(education, dto);
                    dto.setEmployeeId(education.getEmployee() != null ? education.getEmployee().getEmployeeId() : null);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public EducationDTO getEducationById(Long id) {
        Optional<EducationEntity> educationOpt = educationRepo.findById(id);
        if (educationOpt.isEmpty()) return null;
        EducationDTO dto = new EducationDTO();
        BeanUtils.copyProperties(educationOpt.get(), dto);
        dto.setEmployeeId(educationOpt.get().getEmployee() != null ? educationOpt.get().getEmployee().getEmployeeId() : null);
        return dto;
    }
} 
package com.EMS.Employee.Management.System.service.impl;

import com.EMS.Employee.Management.System.dto.SkillDTO;
import com.EMS.Employee.Management.System.entity.EmployeeEntity;
import com.EMS.Employee.Management.System.entity.SkillEntity;
import com.EMS.Employee.Management.System.repo.EmployeeRepo;
import com.EMS.Employee.Management.System.repo.SkillRepo;
import com.EMS.Employee.Management.System.service.SkillService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SkillServiceImpl implements SkillService {
    private final SkillRepo skillRepo;
    private final EmployeeRepo employeeRepo;

    public SkillServiceImpl(SkillRepo skillRepo, EmployeeRepo employeeRepo) {
        this.skillRepo = skillRepo;
        this.employeeRepo = employeeRepo;
    }

    @Override
    public SkillDTO addSkill(SkillDTO skillDTO) {
        SkillEntity skillEntity = new SkillEntity();
        BeanUtils.copyProperties(skillDTO, skillEntity);
        if (skillDTO.getEmployeeId() != null) {
            EmployeeEntity employee = employeeRepo.findById(skillDTO.getEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Employee not found"));
            skillEntity.setEmployee(employee);
        }
        SkillEntity saved = skillRepo.save(skillEntity);
        SkillDTO dto = new SkillDTO();
        BeanUtils.copyProperties(saved, dto);
        dto.setEmployeeId(saved.getEmployee() != null ? saved.getEmployee().getEmployeeId() : null);
        return dto;
    }

    @Override
    public SkillDTO updateSkill(Long id, SkillDTO skillDTO) {
        SkillEntity skillEntity = skillRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Skill not found"));
        if (skillDTO.getName() != null) skillEntity.setName(skillDTO.getName());
        SkillEntity saved = skillRepo.save(skillEntity);
        SkillDTO dto = new SkillDTO();
        BeanUtils.copyProperties(saved, dto);
        dto.setEmployeeId(saved.getEmployee() != null ? saved.getEmployee().getEmployeeId() : null);
        return dto;
    }

    @Override
    public void deleteSkill(Long id) {
        skillRepo.deleteById(id);
    }

    @Override
    public List<SkillDTO> getSkillsByEmployeeId(Integer employeeId) {
        return skillRepo.findByEmployee_EmployeeId(employeeId)
                .stream()
                .map(skill -> {
                    SkillDTO dto = new SkillDTO();
                    BeanUtils.copyProperties(skill, dto);
                    dto.setEmployeeId(skill.getEmployee() != null ? skill.getEmployee().getEmployeeId() : null);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public SkillDTO getSkillById(Long id) {
        Optional<SkillEntity> skillOpt = skillRepo.findById(id);
        if (skillOpt.isEmpty()) return null;
        SkillDTO dto = new SkillDTO();
        BeanUtils.copyProperties(skillOpt.get(), dto);
        dto.setEmployeeId(skillOpt.get().getEmployee() != null ? skillOpt.get().getEmployee().getEmployeeId() : null);
        return dto;
    }
} 
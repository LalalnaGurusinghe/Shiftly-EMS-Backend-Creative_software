package com.EMS.Employee.Management.System.service;

import com.EMS.Employee.Management.System.dto.SkillDTO;
import java.util.List;

public interface SkillService {
    SkillDTO addSkill(SkillDTO skillDTO);
    SkillDTO updateSkill(Long id, SkillDTO skillDTO);
    void deleteSkill(Long id);
    List<SkillDTO> getSkillsByEmployeeId(Integer employeeId);
    SkillDTO getSkillById(Long id);
} 
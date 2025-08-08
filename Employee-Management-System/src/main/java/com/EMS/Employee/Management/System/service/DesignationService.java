package com.EMS.Employee.Management.System.service;

import com.EMS.Employee.Management.System.dto.DesignationDTO;
import java.util.List;

public interface DesignationService {
    DesignationDTO createDesignation(DesignationDTO dto);
    DesignationDTO updateDesignation(Long id, DesignationDTO dto);
    void deleteDesignation(Long id);
    List<DesignationDTO> getAllDesignations();
    DesignationDTO getDesignationById(Long id);
    List<DesignationDTO> getDesignationsByDepartmentId(Long departmentId);
}
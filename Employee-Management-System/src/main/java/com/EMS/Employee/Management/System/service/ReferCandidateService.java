package com.EMS.Employee.Management.System.service;

import java.util.List;

import com.EMS.Employee.Management.System.dto.ClaimDTO;
import org.springframework.web.multipart.MultipartFile;

import com.EMS.Employee.Management.System.dto.ReferCandidateDTO;

public interface ReferCandidateService {
    ReferCandidateDTO create(int employeeId,Long vacancyId, String applicantName, String applicantEmail, String message,
            MultipartFile file) throws Exception;

    List<ReferCandidateDTO> getRefersForAdmin(Long adminUserId);
    List<ReferCandidateDTO> getByEmployeeId(int employeeId);
    List<ReferCandidateDTO> getAllRefers();
    void deleteRefer(Long id);
    ReferCandidateDTO updateStatus(Long id,String status);

    ReferCandidateDTO updateRefer(Long id, Long vacancyId, String applicantName, String applicantEmail,
            String message, MultipartFile file) throws Exception;

}
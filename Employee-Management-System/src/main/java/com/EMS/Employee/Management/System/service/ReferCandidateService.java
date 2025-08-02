package com.EMS.Employee.Management.System.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.EMS.Employee.Management.System.dto.ReferCandidateDTO;

public interface ReferCandidateService {
    ReferCandidateDTO createReferral(Long vacancyId, String applicantName, String applicantEmail, String message,
            MultipartFile file, String status, String username) throws Exception;

    List<ReferCandidateDTO> getOwnReferrals(String username);

    ReferCandidateDTO updateOwnReferral(Long id, ReferCandidateDTO dto, String username);

    void deleteOwnReferral(Long id, String username);

    List<ReferCandidateDTO> getAllReferrals();

    ReferCandidateDTO updateReferralStatus(Long id, String status);

    List<ReferCandidateDTO> getReferralsByUserId(Long userId);

    ReferCandidateDTO updateReferral(Long id, Long vacancyId, String applicantName, String applicantEmail,
            String message, org.springframework.web.multipart.MultipartFile file, String status) throws Exception;

    void deleteReferral(Long id);
}
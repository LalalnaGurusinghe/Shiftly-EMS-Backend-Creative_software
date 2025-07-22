package com.EMS.Employee.Management.System.service;

import com.EMS.Employee.Management.System.dto.ReferCandidateDTO;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface ReferCandidateService {
    ReferCandidateDTO createReferral(Long vacancyId, String applicantName, String applicantEmail, String message,
            MultipartFile file, String status, String username) throws Exception;

    List<ReferCandidateDTO> getOwnReferrals(String username);

    ReferCandidateDTO updateOwnReferral(Long id, ReferCandidateDTO dto, String username);

    void deleteOwnReferral(Long id, String username);

    List<ReferCandidateDTO> getAllReferrals();

    ReferCandidateDTO updateReferralStatus(Long id, String status);
}
package com.EMS.Employee.Management.System.service;

import com.EMS.Employee.Management.System.dto.ReferCandidateDTO;
import java.util.List;

public interface ReferCandidateService {
    // Employee
    ReferCandidateDTO createReferral(ReferCandidateDTO dto, String username);
    List<ReferCandidateDTO> getOwnReferrals(String username);
    ReferCandidateDTO updateOwnReferral(Long id, ReferCandidateDTO dto, String username);
    void deleteOwnReferral(Long id, String username);

    // Admin
    List<ReferCandidateDTO> getAllReferrals();
    ReferCandidateDTO updateReferralStatus(Long id, String status);
} 
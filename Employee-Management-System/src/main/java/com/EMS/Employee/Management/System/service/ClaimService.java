package com.EMS.Employee.Management.System.service;

import com.EMS.Employee.Management.System.dto.ClaimDTO;
import java.util.List;

public interface ClaimService {
    // Employee
    ClaimDTO createClaim(ClaimDTO dto, String username);
    List<ClaimDTO> getOwnClaims(String username);
    ClaimDTO updateOwnClaim(Long id, ClaimDTO dto, String username);
    void deleteOwnClaim(Long id, String username);

    // Admin
    List<ClaimDTO> getAllClaims();
    ClaimDTO updateClaimStatus(Long id, String status);
} 
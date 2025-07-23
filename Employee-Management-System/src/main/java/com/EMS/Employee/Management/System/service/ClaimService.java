package com.EMS.Employee.Management.System.service;

import com.EMS.Employee.Management.System.dto.ClaimDTO;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.time.LocalDate;

public interface ClaimService {
    ClaimDTO createClaim(String claimType, String description, MultipartFile file, String status, String username,
            LocalDate claimDate)
            throws Exception;

    List<ClaimDTO> getAllClaims();

    List<ClaimDTO> getClaimsByUserId(Long userId);

    ClaimDTO updateClaim(Long id, ClaimDTO claimDTO, String username);

    void deleteClaim(Long id, String username);

    ClaimDTO getClaimById(Long id);

    ClaimDTO approveClaim(Long id);

    ClaimDTO rejectClaim(Long id);
}
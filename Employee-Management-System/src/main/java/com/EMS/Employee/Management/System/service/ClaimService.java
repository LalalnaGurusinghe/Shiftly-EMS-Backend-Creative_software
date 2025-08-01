package com.EMS.Employee.Management.System.service;

import com.EMS.Employee.Management.System.dto.ClaimDTO;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.time.LocalDate;

public interface ClaimService {
        ClaimDTO createClaim(String claimType, String description, MultipartFile file, String status, String username,
                        LocalDate claimDate)
                        throws Exception;

        List<ClaimDTO> getAllClaims(String username);

        List<ClaimDTO> getClaimsByUserId(Long userId);

        List<ClaimDTO> getOwnClaims(String username);

        ClaimDTO updateClaim(Long id, String claimType, String description, MultipartFile file, String status,
                        LocalDate claimDate, String username) throws Exception;

        void deleteClaim(Long id, String username);

        ClaimDTO getClaimById(Long id);

        ClaimDTO approveClaim(Long id);

        ClaimDTO rejectClaim(Long id);
}
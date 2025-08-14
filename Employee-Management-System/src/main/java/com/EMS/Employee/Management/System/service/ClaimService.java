package com.EMS.Employee.Management.System.service;

import com.EMS.Employee.Management.System.dto.ClaimDTO;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.time.LocalDate;

public interface ClaimService {
        ClaimDTO create(int employeeId,String claimType, String description, MultipartFile file,
                        String claimDate)
                        throws Exception;

        List<ClaimDTO> getClaimsForAdmin(Long adminUserId);
        List<ClaimDTO> getByEmployeeId(int employeeId);
        List<ClaimDTO> getAllClaims();
        void deleteClaim(Long id);
        ClaimDTO updateStatus(Long id,String status);

        ClaimDTO updateClaim(Long id, String claimType, String description, MultipartFile file,
                             String claimDate) throws Exception;
}
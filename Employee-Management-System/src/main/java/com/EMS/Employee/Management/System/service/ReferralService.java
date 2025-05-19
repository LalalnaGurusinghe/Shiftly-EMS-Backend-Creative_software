package com.EMS.Employee.Management.System.service;

import com.EMS.Employee.Management.System.dto.ReferralDTO;
import com.EMS.Employee.Management.System.entity.Referral;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ReferralService {
    Referral saveReferral(ReferralDTO dto, MultipartFile resume) throws IOException;
    public List<Referral> getAllReferrals();
    public List<ReferralDTO> getAllReferralsAsDTO();
}

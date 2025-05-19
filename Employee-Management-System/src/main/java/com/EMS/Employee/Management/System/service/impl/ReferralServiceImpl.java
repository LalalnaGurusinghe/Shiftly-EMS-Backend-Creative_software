package com.EMS.Employee.Management.System.service.impl;

import com.EMS.Employee.Management.System.dto.ReferralDTO;
import com.EMS.Employee.Management.System.entity.Referral;
import com.EMS.Employee.Management.System.repo.ReferralRepository;
import com.EMS.Employee.Management.System.service.ReferralService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReferralServiceImpl implements ReferralService {
    private final ReferralRepository referralRepository;

    @Override
    public Referral saveReferral(ReferralDTO dto, MultipartFile resume) throws IOException {
        if (resume == null || resume.isEmpty()) {
            throw new IllegalArgumentException("Resume file is required");
        }

        Referral referral = new Referral();
        referral.setVacancy(dto.getVacancy());
        referral.setApplicantName(dto.getApplicantName());
        referral.setApplicantEmail(dto.getApplicantEmail());
        referral.setMessage(dto.getMessage());
        referral.setResume(resume.getBytes());

        return referralRepository.save(referral);
    }

    @Override
    public List<Referral> getAllReferrals() {
        return referralRepository.findAll();
    }

    @Override
    public List<ReferralDTO> getAllReferralsAsDTO() {
        return referralRepository.findAll().stream()
                .map(r -> new ReferralDTO(
                        r.getId(),
                        r.getVacancy(),
                        r.getApplicantName(),
                        r.getApplicantEmail(),
                        r.getMessage(),
                        "resume_" + r.getId() + ".pdf"
                ))
                .collect(Collectors.toList());
    }
}
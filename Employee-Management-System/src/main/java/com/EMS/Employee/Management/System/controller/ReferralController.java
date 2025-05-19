package com.EMS.Employee.Management.System.controller;

import com.EMS.Employee.Management.System.dto.ReferralDTO;
import com.EMS.Employee.Management.System.entity.Referral;
import com.EMS.Employee.Management.System.service.ReferralService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/ems/backEnd/api_v1/referrals")
@RequiredArgsConstructor
public class ReferralController {
    private final ReferralService referralService;

    @PostMapping
    public ResponseEntity<Referral> addReferral(
            @RequestPart("referral") ReferralDTO dto,
            @RequestPart("resume") MultipartFile resume) throws IOException {
        Referral savedReferral = referralService.saveReferral(dto, resume);
        return ResponseEntity.ok(savedReferral);
    }

    @GetMapping("/entities")
    public ResponseEntity<List<Referral>> getAllReferrals() {
        return ResponseEntity.ok(referralService.getAllReferrals());
    }

    @GetMapping("/dtos")
    public ResponseEntity<List<ReferralDTO>> getAllReferralsAsDTO() {
        return ResponseEntity.ok(referralService.getAllReferralsAsDTO());
    }
}
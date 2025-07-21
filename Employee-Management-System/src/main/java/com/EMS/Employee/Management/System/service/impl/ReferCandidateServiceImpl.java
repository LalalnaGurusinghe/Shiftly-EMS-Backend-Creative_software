package com.EMS.Employee.Management.System.service.impl;

import com.EMS.Employee.Management.System.dto.ReferCandidateDTO;
import com.EMS.Employee.Management.System.entity.ReferCandidateEntity;
import com.EMS.Employee.Management.System.entity.ReferStatus;
import com.EMS.Employee.Management.System.entity.User;
import com.EMS.Employee.Management.System.entity.VacancyEntity;
import com.EMS.Employee.Management.System.entity.EmployeeEntity;
import com.EMS.Employee.Management.System.repo.ReferCandidateRepo;
import com.EMS.Employee.Management.System.repo.UserRepo;
import com.EMS.Employee.Management.System.repo.VacancyRepo;
import com.EMS.Employee.Management.System.repo.EmployeeRepo;
import com.EMS.Employee.Management.System.service.ReferCandidateService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Base64;

@Service
public class ReferCandidateServiceImpl implements ReferCandidateService {
    private final ReferCandidateRepo referCandidateRepo;
    private final UserRepo userRepo;
    private final VacancyRepo vacancyRepo;
    private final EmployeeRepo employeeRepo;

    public ReferCandidateServiceImpl(ReferCandidateRepo referCandidateRepo, UserRepo userRepo, VacancyRepo vacancyRepo, EmployeeRepo employeeRepo) {
        this.referCandidateRepo = referCandidateRepo;
        this.userRepo = userRepo;
        this.vacancyRepo = vacancyRepo;
        this.employeeRepo = employeeRepo;
    }

    // Employee: Create referral
    @Override
    @Transactional
    public ReferCandidateDTO createReferral(ReferCandidateDTO dto, String username) {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        ReferCandidateEntity entity = new ReferCandidateEntity();
        BeanUtils.copyProperties(dto, entity);
        if (dto.getResumeData() != null) {
            entity.setResumeData(Base64.getDecoder().decode(dto.getResumeData()));
        }
        entity.setUser(user);
        entity.setStatus(ReferStatus.UNREAD);
        entity.setReferredBy(user);
        VacancyEntity vacancy = vacancyRepo.findById(dto.getVacancyId()).orElseThrow(() -> new RuntimeException("Vacancy not found"));
        entity.setVacancy(vacancy);
        ReferCandidateEntity saved = referCandidateRepo.save(entity);
        return toDTO(saved);
    }

    // Employee: View own referrals
    @Override
    public List<ReferCandidateDTO> getOwnReferrals(String username) {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        return referCandidateRepo.findByReferredBy_Id(user.getId()).stream().map(this::toDTO).collect(Collectors.toList());
    }

    // Employee: Update own referral
    @Override
    @Transactional
    public ReferCandidateDTO updateOwnReferral(Long id, ReferCandidateDTO dto, String username) {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        ReferCandidateEntity entity = referCandidateRepo.findById(id).orElseThrow(() -> new RuntimeException("Referral not found"));
        if (!entity.getReferredBy().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        if (dto.getApplicantName() != null) entity.setApplicantName(dto.getApplicantName());
        if (dto.getApplicantEmail() != null) entity.setApplicantEmail(dto.getApplicantEmail());
        if (dto.getMessage() != null) entity.setMessage(dto.getMessage());
        if (dto.getVacancyId() != null) {
            VacancyEntity vacancy = vacancyRepo.findById(dto.getVacancyId()).orElseThrow(() -> new RuntimeException("Vacancy not found"));
            entity.setVacancy(vacancy);
        }
        ReferCandidateEntity saved = referCandidateRepo.save(entity);
        return toDTO(saved);
    }

    // Employee: Delete own referral
    @Override
    @Transactional
    public void deleteOwnReferral(Long id, String username) {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        ReferCandidateEntity entity = referCandidateRepo.findById(id).orElseThrow(() -> new RuntimeException("Referral not found"));
        if (!entity.getReferredBy().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        referCandidateRepo.deleteById(id);
    }

    // Admin: View all referrals
    @Override
    public List<ReferCandidateDTO> getAllReferrals() {
        return referCandidateRepo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    // Admin: Update status (Read/Unread)
    @Override
    @Transactional
    public ReferCandidateDTO updateReferralStatus(Long id, String status) {
        ReferCandidateEntity entity = referCandidateRepo.findById(id).orElseThrow(() -> new RuntimeException("Referral not found"));
        entity.setStatus(ReferStatus.valueOf(status));
        ReferCandidateEntity saved = referCandidateRepo.save(entity);
        return toDTO(saved);
    }

    // Helper: Entity to DTO
    private ReferCandidateDTO toDTO(ReferCandidateEntity entity) {
        ReferCandidateDTO dto = new ReferCandidateDTO();
        BeanUtils.copyProperties(entity, dto);
        if (entity.getResumeData() != null) {
            dto.setResumeData(Base64.getEncoder().encodeToString(entity.getResumeData()));
        }
        dto.setVacancyId(entity.getVacancy() != null ? entity.getVacancy().getId() : null);
        dto.setVacancyName(entity.getVacancy() != null ? entity.getVacancy().getName() : null);
        dto.setReferredById(entity.getReferredBy().getId());
        dto.setReferredByUsername(entity.getReferredBy().getUsername());
        EmployeeEntity emp = employeeRepo.findByUser_Id(entity.getReferredBy().getId());
        dto.setReferredByUserId(entity.getReferredBy().getId());
        dto.setReferredByFirstName(emp != null ? emp.getFirstName() : null);
        dto.setStatus(entity.getStatus() != null ? entity.getStatus().name() : null);
        dto.setUserId(entity.getUser() != null ? entity.getUser().getId() : null);
        return dto;
    }
} 
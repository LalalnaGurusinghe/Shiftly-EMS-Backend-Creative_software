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
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

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

    public ReferCandidateServiceImpl(ReferCandidateRepo referCandidateRepo, UserRepo userRepo, VacancyRepo vacancyRepo,
            EmployeeRepo employeeRepo) {
        this.referCandidateRepo = referCandidateRepo;
        this.userRepo = userRepo;
        this.vacancyRepo = vacancyRepo;
        this.employeeRepo = employeeRepo;
    }

    // Employee: Create referral
    @Override
    @Transactional
    public ReferCandidateDTO createReferral(Long vacancyId, String applicantName, String applicantEmail, String message,
            MultipartFile file, String status, String username) throws Exception {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        VacancyEntity vacancy = vacancyRepo.findById(vacancyId)
                .orElseThrow(() -> new RuntimeException("Vacancy not found"));

        ReferCandidateEntity entity = new ReferCandidateEntity();
        entity.setVacancy(vacancy);
        entity.setApplicantName(applicantName);
        entity.setApplicantEmail(applicantEmail);
        entity.setMessage(message);
        entity.setStatus(status != null ? ReferStatus.valueOf(status) : ReferStatus.UNREAD);
        entity.setUser(user);

        if (file != null && !file.isEmpty()) {
            String projectRoot = System.getProperty("user.dir");
            String uploadDir = projectRoot + java.io.File.separator + "uploads" + java.io.File.separator + "files"
                    + java.io.File.separator;
            Files.createDirectories(Paths.get(uploadDir));
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            System.out.println("Saving file to: " + filePath.toAbsolutePath());
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            entity.setFileUrl("/uploads/files/" + fileName);
        }

        ReferCandidateEntity saved = referCandidateRepo.save(entity);
        return toDTO(saved);
    }

    // Employee: View own referrals
    @Override
    public List<ReferCandidateDTO> getOwnReferrals(String username) {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        return referCandidateRepo.findByUser_Id(user.getId()).stream().map(this::toDTO).collect(Collectors.toList());
    }

    // Employee: Update own referral
    @Override
    @Transactional
    public ReferCandidateDTO updateOwnReferral(Long id, ReferCandidateDTO dto, String username) {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        ReferCandidateEntity entity = referCandidateRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Referral not found"));
        if (!entity.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        if (dto.getApplicantName() != null)
            entity.setApplicantName(dto.getApplicantName());
        if (dto.getApplicantEmail() != null)
            entity.setApplicantEmail(dto.getApplicantEmail());
        if (dto.getMessage() != null)
            entity.setMessage(dto.getMessage());
        if (dto.getVacancyId() != null) {
            VacancyEntity vacancy = vacancyRepo.findById(dto.getVacancyId())
                    .orElseThrow(() -> new RuntimeException("Vacancy not found"));
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
        ReferCandidateEntity entity = referCandidateRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Referral not found"));
        if (!entity.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        referCandidateRepo.deleteById(id);
    }

    // Admin: View all referrals
    @Override
    public List<ReferCandidateDTO> getAllReferrals() {
        return referCandidateRepo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ReferCandidateDTO updateReferralStatus(Long id, String status) {
        ReferCandidateEntity entity = referCandidateRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Referral not found"));
        entity.setStatus(ReferStatus.valueOf(status));
        ReferCandidateEntity saved = referCandidateRepo.save(entity);
        return toDTO(saved);
    }

    @Override
    @Transactional
    public ReferCandidateDTO updateReferral(Long id, Long vacancyId, String applicantName, String applicantEmail,
            String message, MultipartFile file, String status) throws Exception {
        ReferCandidateEntity entity = referCandidateRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Referral not found"));
        if (vacancyId != null) {
            VacancyEntity vacancy = vacancyRepo.findById(vacancyId)
                    .orElseThrow(() -> new RuntimeException("Vacancy not found"));
            entity.setVacancy(vacancy);
        }
        if (applicantName != null)
            entity.setApplicantName(applicantName);
        if (applicantEmail != null)
            entity.setApplicantEmail(applicantEmail);
        if (message != null)
            entity.setMessage(message);
        if (status != null)
            entity.setStatus(com.EMS.Employee.Management.System.entity.ReferStatus.valueOf(status));
        if (file != null && !file.isEmpty()) {
            String projectRoot = System.getProperty("user.dir");
            String uploadDir = projectRoot + java.io.File.separator + "uploads" + java.io.File.separator + "files"
                    + java.io.File.separator;
            Files.createDirectories(Paths.get(uploadDir));
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            entity.setFileUrl("/uploads/files/" + fileName);
        }
        ReferCandidateEntity saved = referCandidateRepo.save(entity);
        return toDTO(saved);
    }

    @Override
    public List<ReferCandidateDTO> getReferralsByUserId(Long userId) {
        return referCandidateRepo.findByUser_Id(userId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteReferral(Long id) {
        referCandidateRepo.deleteById(id);
    }

    // Helper: Entity to DTO
    private ReferCandidateDTO toDTO(ReferCandidateEntity entity) {
        ReferCandidateDTO dto = new ReferCandidateDTO();
        dto.setId(entity.getId());
        dto.setVacancyId(entity.getVacancy() != null ? entity.getVacancy().getId() : null);
        dto.setApplicantName(entity.getApplicantName());
        dto.setApplicantEmail(entity.getApplicantEmail());
        dto.setMessage(entity.getMessage());
        dto.setFileUrl(entity.getFileUrl());
        dto.setStatus(entity.getStatus() != null ? entity.getStatus().name() : null);
        dto.setUserId(entity.getUser() != null ? entity.getUser().getId() : null);
        // Set departmentName using EmployeeEntity
        if (entity.getUser() != null) {
            EmployeeEntity employee = employeeRepo.findByUser_Id(entity.getUser().getId());
            if (employee != null) {
                dto.setDepartmentName(employee.getDepartment());
            }
        }
        return dto;
    }
}
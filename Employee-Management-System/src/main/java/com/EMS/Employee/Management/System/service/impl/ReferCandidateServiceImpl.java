package com.EMS.Employee.Management.System.service.impl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.EMS.Employee.Management.System.dto.ClaimDTO;
import com.EMS.Employee.Management.System.entity.*;
import com.EMS.Employee.Management.System.repo.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.EMS.Employee.Management.System.dto.ReferCandidateDTO;
import com.EMS.Employee.Management.System.service.ReferCandidateService;

@Service
public class ReferCandidateServiceImpl implements ReferCandidateService {
    private final ReferCandidateRepo referCandidateRepo;
    private final VacancyRepo vacancyRepo;
    private final EmployeeRepo employeeRepo;
    private final DepartmentRepo departmentRepo;

    public ReferCandidateServiceImpl(ReferCandidateRepo referCandidateRepo,  EmployeeRepo employeeRepo, DepartmentRepo departmentRepo, VacancyRepo vacancyRepo) {
        this.referCandidateRepo = referCandidateRepo;
        this.vacancyRepo = vacancyRepo;
        this.employeeRepo = employeeRepo;
        this.departmentRepo = departmentRepo;
    }

    // Employee: Create referral
    @Override
    @Transactional
    public ReferCandidateDTO create(int employeeId,Long vacancyId, String applicantName, String applicantEmail, String message,
            MultipartFile file) throws Exception {
        EmployeeEntity employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        VacancyEntity vacancy = vacancyRepo.findById(vacancyId)
                .orElseThrow(() -> new RuntimeException("Vacancy not found"));

        ReferCandidateEntity entity = new ReferCandidateEntity();
        entity.setEmployee(employee);
        entity.setVacancy(vacancy);
        entity.setApplicantName(applicantName);
        entity.setApplicantEmail(applicantEmail);
        entity.setMessage(message);
        entity.setStatus(ReferStatus.UNREAD);

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

    @Override
    public List<ReferCandidateDTO> getRefersForAdmin(Long adminUserId) {
        DepartmentEntity department = departmentRepo.findByAdmin_Id(adminUserId)
                .stream().findFirst().orElse(null);
        if (department == null)
            return List.of();
        return referCandidateRepo.findByEmployee_Department_Id(department.getId())
                .stream().map(this::toDTO).toList();
    }

    @Override
    public List<ReferCandidateDTO> getAllRefers() {
        return referCandidateRepo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }
    @Override
    public List<ReferCandidateDTO> getByEmployeeId(int employeeId) {
        return referCandidateRepo.findByEmployee_EmployeeId(employeeId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteRefer(Long id) {
        ReferCandidateEntity refer = referCandidateRepo.findById(id).orElseThrow(() -> new
                RuntimeException("Refer not found"));
        referCandidateRepo.delete(refer);
    }

    @Override
    @Transactional
    public ReferCandidateDTO updateStatus(Long id, String status) {
        ReferCandidateEntity refer = referCandidateRepo.findById(id).orElseThrow(() -> new
                RuntimeException("Refer not found"));
        refer.setStatus(ReferStatus.valueOf(status));
        ReferCandidateEntity saved = referCandidateRepo.save(refer);
        return toDTO(saved);
    }

    @Override
    @Transactional
    public ReferCandidateDTO updateRefer(Long id, Long vacancyId, String applicantName, String applicantEmail,
            String message, MultipartFile file) throws Exception {
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



    private ReferCandidateDTO toDTO(ReferCandidateEntity entity) {
        ReferCandidateDTO dto = new ReferCandidateDTO();
        if (entity.getEmployee() != null) {
            dto.setEmployeeId(entity.getEmployee().getEmployeeId());
            dto.setDepartmentName(
                    entity.getEmployee().getDepartment() != null ? entity.getEmployee().getDepartment().getName()
                            : null);
            dto.setEmployeeName(entity.getEmployee().getFirstName() != null ? entity.getEmployee().getFirstName() + " " + entity.getEmployee().getLastName() : null);
        }
        if (entity.getVacancy() != null){
            dto.setVacancyName(entity.getVacancy().getName());
        }
        dto.setId(entity.getId());
        dto.setVacancyName(entity.getVacancy() != null ? entity.getVacancy().getName() : null);
        dto.setApplicantName(entity.getApplicantName());
        dto.setApplicantEmail(entity.getApplicantEmail());
        dto.setMessage(entity.getMessage());
        dto.setFileUrl(entity.getFileUrl());
        dto.setStatus(entity.getStatus().name());
        
        return dto;
    }
}
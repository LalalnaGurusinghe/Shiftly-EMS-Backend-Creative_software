package com.EMS.Employee.Management.System.service.impl;

import com.EMS.Employee.Management.System.dto.ClaimDTO;
import com.EMS.Employee.Management.System.dto.TimesheetDTO;
import com.EMS.Employee.Management.System.entity.*;
import com.EMS.Employee.Management.System.repo.ClaimRepo;
import com.EMS.Employee.Management.System.repo.DepartmentRepo;
import com.EMS.Employee.Management.System.repo.EmployeeRepo;
import com.EMS.Employee.Management.System.repo.UserRepo;
import com.EMS.Employee.Management.System.service.ClaimService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ClaimServiceImpl implements ClaimService {
    private final ClaimRepo claimRepo;
    private final EmployeeRepo employeeRepo;
    private final DepartmentRepo departmentRepo;

    public ClaimServiceImpl(ClaimRepo claimRepo, EmployeeRepo employeeRepo, DepartmentRepo departmentRepo) {
        this.claimRepo = claimRepo;
        this.employeeRepo = employeeRepo;
        this.departmentRepo = departmentRepo;
    }

    @Override
    @Transactional
    public ClaimDTO create(int employeeId,String claimType, String description, MultipartFile file, String claimDate) throws Exception {
        EmployeeEntity employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        ClaimEntity entity = new ClaimEntity();
        entity.setEmployee(employee);
        entity.setClaimType(claimType);
        entity.setDescription(description);
        entity.setStatus(ClaimStatus.PENDING);
        entity.setClaimDate(claimDate);
        if (file != null && !file.isEmpty()) {
            String projectRoot = System.getProperty("user.dir");
            String uploadDir = projectRoot + java.io.File.separator + "uploads" + java.io.File.separator + "files"
                    + java.io.File.separator;
            Files.createDirectories(Paths.get(uploadDir));
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            entity.setClaimUrl("/uploads/files/" + fileName);
        }
        ClaimEntity saved = claimRepo.save(entity);
        return toDTO(saved);
    }

    @Override
    public List<ClaimDTO> getClaimsForAdmin(Long adminUserId) {
        DepartmentEntity department = departmentRepo.findByAdmin_Id(adminUserId)
                .stream().findFirst().orElse(null);
        if (department == null)
            return List.of();
        return claimRepo.findByEmployee_Department_Id(department.getId())
                .stream().map(this::toDTO).toList();
    }

    @Override
    public List<ClaimDTO> getAllClaims() {
        return claimRepo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }
    @Override
    public List<ClaimDTO> getByEmployeeId(int employeeId) {
        return claimRepo.findByEmployee_EmployeeId(employeeId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteClaim(Long id) {
        ClaimEntity claim = claimRepo.findById(id).orElseThrow(() -> new
                RuntimeException("Claim not found"));
        claimRepo.delete(claim);
    }

    @Override
    @Transactional
    public ClaimDTO updateStatus(Long id, String status) {
        ClaimEntity claim = claimRepo.findById(id).orElseThrow(() -> new
                RuntimeException("Claim not found"));
        claim.setStatus(ClaimStatus.valueOf(status));
        ClaimEntity saved = claimRepo.save(claim);
        return toDTO(saved);
    }


    @Override
    @Transactional
    public ClaimDTO updateClaim(Long id, String claimType, String description, MultipartFile file, String claimDate) throws Exception {

        ClaimEntity entity = claimRepo.findById(id).orElseThrow(() -> new RuntimeException("Claim not found"));

        if (claimType != null)
            entity.setClaimType(claimType);
        if (description != null)
            entity.setDescription(description);
        if (claimDate != null)
            entity.setClaimDate(claimDate);

        if (file != null && !file.isEmpty()) {
            String projectRoot = System.getProperty("user.dir");
            String uploadDir = projectRoot + java.io.File.separator + "uploads" + java.io.File.separator + "files"
                    + java.io.File.separator;
            Files.createDirectories(Paths.get(uploadDir));
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            entity.setClaimUrl("/uploads/files/" + fileName);
        }

        ClaimEntity saved = claimRepo.save(entity);
        return toDTO(saved);
    }

    private ClaimDTO toDTO(ClaimEntity entity) {
        ClaimDTO dto = new ClaimDTO();
        if (entity.getEmployee() != null) {
            dto.setEmployeeId(entity.getEmployee().getEmployeeId());
            dto.setDepartmentName(
                    entity.getEmployee().getDepartment() != null ? entity.getEmployee().getDepartment().getName()
                            : null);
            dto.setEmployeeName(entity.getEmployee().getFirstName() != null ? entity.getEmployee().getFirstName() + " " + entity.getEmployee().getLastName() : null);
        }
        dto.setId(entity.getId());
        dto.setClaimType(entity.getClaimType());
        dto.setDescription(entity.getDescription());
        dto.setStatus(entity.getStatus().name());
        dto.setClaimUrl(entity.getClaimUrl());
        dto.setClaimDate(entity.getClaimDate());
        return dto;
    }
}
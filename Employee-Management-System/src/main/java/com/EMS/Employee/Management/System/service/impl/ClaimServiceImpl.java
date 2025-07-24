package com.EMS.Employee.Management.System.service.impl;

import com.EMS.Employee.Management.System.dto.ClaimDTO;
import com.EMS.Employee.Management.System.entity.ClaimEntity;
import com.EMS.Employee.Management.System.entity.ClaimStatus;
import com.EMS.Employee.Management.System.entity.User;
import com.EMS.Employee.Management.System.repo.ClaimRepo;
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
    private final UserRepo userRepo;

    public ClaimServiceImpl(ClaimRepo claimRepo, UserRepo userRepo) {
        this.claimRepo = claimRepo;
        this.userRepo = userRepo;
    }

    @Override
    @Transactional
    public ClaimDTO createClaim(String claimType, String description, MultipartFile file, String status,
            String username, LocalDate claimDate) throws Exception {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        ClaimEntity entity = new ClaimEntity();
        entity.setClaimType(claimType);
        entity.setDescription(description);
        entity.setStatus(status != null ? ClaimStatus.valueOf(status) : ClaimStatus.PENDING);
        entity.setUser(user);
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
public List<ClaimDTO> getAllClaims(String username) {
    User currentUser = userRepo.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("User not found"));
    
    List<ClaimEntity> claims;
    
    // Role-based filtering
    if (currentUser.getRoles() != null && currentUser.getRoles().contains("SUPER_ADMIN")) {
        // Super admin can see all claims
        claims = claimRepo.findAll();
    } else if (currentUser.getRoles() != null && currentUser.getRoles().contains("ADMIN")) {
        // Regular admin can only see claims from their department
        claims = claimRepo.findByUser_Department(currentUser.getDepartment());
    } else {
        // Non-admin users should not access this endpoint
        throw new RuntimeException("Access denied: insufficient privileges");
    }
    
    return claims.stream().map(this::toDTO).collect(Collectors.toList());
}

    @Override
    public List<ClaimDTO> getClaimsByUserId(Long userId) {
        return claimRepo.findByUser_Id(userId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ClaimDTO updateClaim(Long id, ClaimDTO dto, String username) {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        ClaimEntity entity = claimRepo.findById(id).orElseThrow(() -> new RuntimeException("Claim not found"));
        if (!entity.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        if (dto.getClaimType() != null)
            entity.setClaimType(dto.getClaimType());
        if (dto.getDescription() != null)
            entity.setDescription(dto.getDescription());
        if (dto.getStatus() != null)
            entity.setStatus(ClaimStatus.valueOf(dto.getStatus()));
        if (dto.getClaimDate() != null)
            entity.setClaimDate(dto.getClaimDate());
        ClaimEntity saved = claimRepo.save(entity);
        return toDTO(saved);
    }

    @Override
    @Transactional
    public void deleteClaim(Long id, String username) {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        ClaimEntity entity = claimRepo.findById(id).orElseThrow(() -> new RuntimeException("Claim not found"));
        if (!entity.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        claimRepo.deleteById(id);
    }

    @Override
    public ClaimDTO getClaimById(Long id) {
        ClaimEntity entity = claimRepo.findById(id).orElseThrow(() -> new RuntimeException("Claim not found"));
        return toDTO(entity);
    }

    @Override
    @Transactional
    public ClaimDTO approveClaim(Long id) {
        ClaimEntity entity = claimRepo.findById(id).orElseThrow(() -> new RuntimeException("Claim not found"));
        entity.setStatus(ClaimStatus.APPROVED);
        ClaimEntity saved = claimRepo.save(entity);
        return toDTO(saved);
    }

    @Override
    @Transactional
    public ClaimDTO rejectClaim(Long id) {
        ClaimEntity entity = claimRepo.findById(id).orElseThrow(() -> new RuntimeException("Claim not found"));
        entity.setStatus(ClaimStatus.REJECTED);
        ClaimEntity saved = claimRepo.save(entity);
        return toDTO(saved);
    }

    private ClaimDTO toDTO(ClaimEntity entity) {
        ClaimDTO dto = new ClaimDTO();
        dto.setId(entity.getId());
        dto.setClaimType(entity.getClaimType());
        dto.setDescription(entity.getDescription());
        dto.setStatus(entity.getStatus() != null ? entity.getStatus().name() : null);
        dto.setUserId(entity.getUser() != null ? entity.getUser().getId() : null);
        dto.setClaimUrl(entity.getClaimUrl());
        dto.setClaimDate(entity.getClaimDate());

        // Add user details including department
        if (entity.getUser() != null) {
            dto.setEmployeeName(entity.getUser().getUsername());
            dto.setEmployeeEmail(entity.getUser().getEmail());
            dto.setDepartment(entity.getUser().getDepartment());
        }

        return dto;
    }
}
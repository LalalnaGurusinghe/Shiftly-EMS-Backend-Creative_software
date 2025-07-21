package com.EMS.Employee.Management.System.service.impl;

import com.EMS.Employee.Management.System.dto.ClaimDTO;
import com.EMS.Employee.Management.System.entity.ClaimEntity;
import com.EMS.Employee.Management.System.entity.ClaimStatus;
import com.EMS.Employee.Management.System.entity.User;
import com.EMS.Employee.Management.System.entity.EmployeeEntity;
import com.EMS.Employee.Management.System.repo.ClaimRepo;
import com.EMS.Employee.Management.System.repo.UserRepo;
import com.EMS.Employee.Management.System.repo.EmployeeRepo;
import com.EMS.Employee.Management.System.service.ClaimService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClaimServiceImpl implements ClaimService {
    private final ClaimRepo claimRepo;
    private final UserRepo userRepo;
    private final EmployeeRepo employeeRepo;

    public ClaimServiceImpl(ClaimRepo claimRepo, UserRepo userRepo, EmployeeRepo employeeRepo) {
        this.claimRepo = claimRepo;
        this.userRepo = userRepo;
        this.employeeRepo = employeeRepo;
    }

    // Employee: Create claim
    @Override
    @Transactional
    public ClaimDTO createClaim(ClaimDTO dto, String username) {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        ClaimEntity entity = new ClaimEntity();
        BeanUtils.copyProperties(dto, entity);
        entity.setRequestedBy(user);
        entity.setStatus(ClaimStatus.PENDING);
        ClaimEntity saved = claimRepo.save(entity);
        return toDTO(saved);
    }

    // Employee: View own claims
    @Override
    public List<ClaimDTO> getOwnClaims(String username) {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        return claimRepo.findByRequestedBy_Id(user.getId()).stream().map(this::toDTO).collect(Collectors.toList());
    }

    // Employee: Update own claim
    @Override
    @Transactional
    public ClaimDTO updateOwnClaim(Long id, ClaimDTO dto, String username) {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        ClaimEntity entity = claimRepo.findById(id).orElseThrow(() -> new RuntimeException("Claim not found"));
        if (!entity.getRequestedBy().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        if (dto.getClaimType() != null) entity.setClaimType(dto.getClaimType());
        if (dto.getDescription() != null) entity.setDescription(dto.getDescription());
        if (dto.getFileName() != null) entity.setFileName(dto.getFileName());
        if (dto.getFilePath() != null) entity.setFilePath(dto.getFilePath());
        ClaimEntity saved = claimRepo.save(entity);
        return toDTO(saved);
    }

    // Employee: Delete own claim
    @Override
    @Transactional
    public void deleteOwnClaim(Long id, String username) {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        ClaimEntity entity = claimRepo.findById(id).orElseThrow(() -> new RuntimeException("Claim not found"));
        if (!entity.getRequestedBy().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        claimRepo.deleteById(id);
    }

    // Admin: View all claims
    @Override
    public List<ClaimDTO> getAllClaims() {
        return claimRepo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    // Admin: Approve/Reject/Update status
    @Override
    @Transactional
    public ClaimDTO updateClaimStatus(Long id, String status) {
        ClaimEntity entity = claimRepo.findById(id).orElseThrow(() -> new RuntimeException("Claim not found"));
        entity.setStatus(ClaimStatus.valueOf(status));
        ClaimEntity saved = claimRepo.save(entity);
        return toDTO(saved);
    }

    // Helper: Entity to DTO
    private ClaimDTO toDTO(ClaimEntity entity) {
        ClaimDTO dto = new ClaimDTO();
        BeanUtils.copyProperties(entity, dto);
        dto.setRequestedById(entity.getRequestedBy().getId());
        dto.setRequestedByUsername(entity.getRequestedBy().getUsername());
        EmployeeEntity emp = employeeRepo.findByUser_Id(entity.getRequestedBy().getId());
        dto.setRequestedByFirstName(emp != null ? emp.getFirstName() : null);
        return dto;
    }
} 
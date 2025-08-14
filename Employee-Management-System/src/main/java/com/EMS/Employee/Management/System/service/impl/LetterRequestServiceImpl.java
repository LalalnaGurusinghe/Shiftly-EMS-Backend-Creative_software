package com.EMS.Employee.Management.System.service.impl;

import com.EMS.Employee.Management.System.dto.LetterRequestDto;
import com.EMS.Employee.Management.System.entity.LetterRequest;
import com.EMS.Employee.Management.System.entity.LetterRequestStatus;
import com.EMS.Employee.Management.System.repo.LetterRequestRepo;
import com.EMS.Employee.Management.System.repo.EmployeeRepo;
import com.EMS.Employee.Management.System.repo.DepartmentRepo;
import com.EMS.Employee.Management.System.service.AiLetterService;
import com.EMS.Employee.Management.System.service.LetterRequestService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LetterRequestServiceImpl implements LetterRequestService {
    private final LetterRequestRepo letterRequestRepo;
    private final EmployeeRepo employeeRepo;
    private final DepartmentRepo departmentRepo;
    private final AiLetterService aiLetterService;
    private final ObjectMapper objectMapper;

    public LetterRequestServiceImpl(LetterRequestRepo letterRequestRepo, EmployeeRepo employeeRepo, DepartmentRepo departmentRepo, AiLetterService aiLetterService, ObjectMapper objectMapper) {
        this.letterRequestRepo = letterRequestRepo;
        this.employeeRepo = employeeRepo;
        this.departmentRepo = departmentRepo;
        this.aiLetterService = aiLetterService;
        this.objectMapper = objectMapper;
    }

    private LetterRequestDto toDto(LetterRequest entity) {
        LetterRequestDto dto = new LetterRequestDto();
        dto.setId(entity.getId());
        dto.setLetterType(entity.getLetterType());
        dto.setStatus(entity.getStatus() != null ? entity.getStatus().name() : null);
        dto.setGeneratedLetterHtml(entity.getGeneratedLetterHtml());
        try {
            dto.setFields(objectMapper.readValue(entity.getFieldsJson(), java.util.Map.class));
        } catch (Exception e) {
            dto.setFields(null);
        }
        if (entity.getEmployee() != null) {
            dto.setEmployeeId(entity.getEmployee().getEmployeeId());
            dto.setDepartmentName(entity.getEmployee().getDepartment() != null ? entity.getEmployee().getDepartment().getName() : null);
            dto.setEmployeeName(entity.getEmployee().getFirstName() != null ? entity.getEmployee().getFirstName() + " " + entity.getEmployee().getLastName() : null);
        }
        return dto;
    }

    @Override
    public LetterRequestDto createRequest(int employeeId, LetterRequestDto dto) {
        var employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        LetterRequest entity = new LetterRequest();
        entity.setLetterType(dto.getLetterType());
        try {
            entity.setFieldsJson(objectMapper.writeValueAsString(dto.getFields()));
        } catch (Exception e) {
            throw new RuntimeException("Invalid fields", e);
        }
        entity.setRequestedAt(LocalDateTime.now());
        entity.setStatus(LetterRequestStatus.UNREAD);
        entity.setEmployee(employee);
        LetterRequest saved = letterRequestRepo.save(entity);
        return toDto(saved);
    }

    @Override
    public List<LetterRequestDto> getByEmployeeId(int employeeId) {
        return letterRequestRepo.findByEmployee_EmployeeId(employeeId)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<LetterRequestDto> getAllRequests() {
        List<LetterRequest> requests = letterRequestRepo.findAll();
        boolean updated = false;
        for (LetterRequest req : requests) {
            if (req.getStatus() == LetterRequestStatus.UNREAD) {
                req.setStatus(LetterRequestStatus.READ);
                updated = true;
            }
        }
        if (updated) {
            letterRequestRepo.saveAll(requests);
        }
        return requests.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<LetterRequestDto> getRequestsForAdmin(Long adminUserId) {
        var department = departmentRepo.findByAdmin_Id(adminUserId)
                .stream().findFirst().orElse(null);
        if (department == null)
            return List.of();
        return letterRequestRepo.findByEmployee_Department_Id(department.getId())
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public String generateLetter(Long requestId) {
        LetterRequest entity = letterRequestRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        if (entity.getGeneratedLetterHtml() != null) {
            return entity.getGeneratedLetterHtml();
        }
        try {
            java.util.Map<String, Object> fields = objectMapper.readValue(entity.getFieldsJson(), java.util.Map.class);
            String html = aiLetterService.generateLetter(entity.getLetterType(), fields);
            entity.setGeneratedLetterHtml(html);
            letterRequestRepo.save(entity);
            return html;
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate letter", e);
        }
    }

    @Override
    public void deleteRequest(Long id) {
        LetterRequest entity = letterRequestRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        letterRequestRepo.delete(entity);
    }

    @Override
    public LetterRequestDto updateStatus(Long id, String status) {
        LetterRequest entity = letterRequestRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        entity.setStatus(LetterRequestStatus.valueOf(status));
        LetterRequest saved = letterRequestRepo.save(entity);
        return toDto(saved);
    }

    @Override
    public LetterRequestDto updateRequest(Long id, LetterRequestDto dto) {
        LetterRequest entity = letterRequestRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        if (dto.getLetterType() != null) entity.setLetterType(dto.getLetterType());
        if (dto.getFields() != null) {
            try {
                entity.setFieldsJson(objectMapper.writeValueAsString(dto.getFields()));
            } catch (Exception e) {
                throw new RuntimeException("Invalid fields", e);
            }
        }
        if (dto.getStatus() != null) entity.setStatus(LetterRequestStatus.valueOf(dto.getStatus()));
        LetterRequest saved = letterRequestRepo.save(entity);
        return toDto(saved);
    }
}
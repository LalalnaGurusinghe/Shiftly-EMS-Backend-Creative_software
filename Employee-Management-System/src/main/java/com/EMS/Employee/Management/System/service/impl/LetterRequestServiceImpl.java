 
package com.EMS.Employee.Management.System.service.impl;

import com.EMS.Employee.Management.System.dto.LetterRequestDto;
import com.EMS.Employee.Management.System.entity.LetterRequest;
import com.EMS.Employee.Management.System.entity.LetterRequestStatus;
import com.EMS.Employee.Management.System.repo.LetterRequestRepo;
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
    private final AiLetterService aiLetterService;
    private final ObjectMapper objectMapper;

    public LetterRequestServiceImpl(LetterRequestRepo letterRequestRepo, AiLetterService aiLetterService, ObjectMapper objectMapper) {
        this.letterRequestRepo = letterRequestRepo;
        this.aiLetterService = aiLetterService;
        this.objectMapper = objectMapper;
    }

    private LetterRequestDto toDto(LetterRequest entity) {
        LetterRequestDto dto = new LetterRequestDto();
        dto.setId(entity.getId());
        dto.setLetterType(entity.getLetterType());
        dto.setStatus(entity.getStatus() != null ? entity.getStatus().name() : null);
        dto.setRequestedBy(entity.getRequestedBy());
        dto.setGeneratedLetterHtml(entity.getGeneratedLetterHtml());
        try {
            dto.setFields(objectMapper.readValue(entity.getFieldsJson(), java.util.Map.class));
        } catch (Exception e) {
            dto.setFields(null);
        }
        return dto;
    }

    @Override
    public LetterRequestDto createRequest(LetterRequestDto dto, String username) {
        LetterRequest entity = new LetterRequest();
        entity.setLetterType(dto.getLetterType());
        try {
            entity.setFieldsJson(objectMapper.writeValueAsString(dto.getFields()));
        } catch (Exception e) {
            throw new RuntimeException("Invalid fields", e);
        }
        entity.setRequestedBy(username);
        entity.setRequestedAt(LocalDateTime.now());
        entity.setStatus(LetterRequestStatus.UNREAD);
        LetterRequest saved = letterRequestRepo.save(entity);
        return toDto(saved);
    }

    @Override
    public List<LetterRequestDto> getUserRequests(String username) {
        return letterRequestRepo.findByRequestedBy(username)
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
    public void deleteRequest(Long id, String username) {
        LetterRequest entity = letterRequestRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        // Only allow delete if the user is the owner (or add admin check if needed)
        if (!entity.getRequestedBy().equals(username)) {
            throw new RuntimeException("You are not authorized to delete this request");
        }
        letterRequestRepo.deleteById(id);
    }
}

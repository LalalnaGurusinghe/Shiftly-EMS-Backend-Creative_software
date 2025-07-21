package com.EMS.Employee.Management.System.service.impl;

import com.EMS.Employee.Management.System.dto.EventDTO;
import com.EMS.Employee.Management.System.entity.EmployeeEntity;
import com.EMS.Employee.Management.System.entity.EventEntity;
import com.EMS.Employee.Management.System.entity.EventStatus;
import com.EMS.Employee.Management.System.repo.EmployeeRepo;
import com.EMS.Employee.Management.System.repo.EventRepo;
import com.EMS.Employee.Management.System.service.EventService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {
    private final EventRepo eventRepo;
    private final EmployeeRepo employeeRepo;

    public EventServiceImpl(EventRepo eventRepo, EmployeeRepo employeeRepo) {
        this.eventRepo = eventRepo;
        this.employeeRepo = employeeRepo;
    }

    @Override
    public EventDTO createEvent(EventDTO eventDTO) {
        EventEntity eventEntity = new EventEntity();
        BeanUtils.copyProperties(eventDTO, eventEntity);
        if (eventDTO.getFileData() != null) {
            eventEntity.setFileData(Base64.getDecoder().decode(eventDTO.getFileData()));
        }
        if (eventDTO.getCreatedBy() != null) {
            EmployeeEntity employee = employeeRepo.findById(eventDTO.getCreatedBy())
                    .orElseThrow(() -> new RuntimeException("Employee not found"));
            eventEntity.setCreatedBy(employee);
            if (employee.getUser() != null) {
                eventEntity.setUser(employee.getUser());
            }
        }
        eventEntity.setStatus(EventStatus.PENDING);
        EventEntity saved = eventRepo.save(eventEntity);
        EventDTO dto = toDTO(saved);
        return dto;
    }

    @Override
    public EventDTO updateEvent(Long id, EventDTO eventDTO) {
        EventEntity eventEntity = eventRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        if (eventDTO.getTitle() != null) eventEntity.setTitle(eventDTO.getTitle());
        if (eventDTO.getEventType() != null) eventEntity.setEventType(eventDTO.getEventType());
        if (eventDTO.getEnableDate() != null) eventEntity.setEnableDate(eventDTO.getEnableDate());
        if (eventDTO.getExpireDate() != null) eventEntity.setExpireDate(eventDTO.getExpireDate());
        EventEntity saved = eventRepo.save(eventEntity);
        EventDTO dto = new EventDTO();
        BeanUtils.copyProperties(saved, dto);
        dto.setCreatedBy(saved.getCreatedBy() != null ? saved.getCreatedBy().getEmployeeId() : null);
        dto.setStatus(saved.getStatus().name());
        return dto;
    }

    @Override
    public void deleteEvent(Long id) {
        eventRepo.deleteById(id);
    }

    @Override
    public EventDTO getEventById(Long id) {
        Optional<EventEntity> eventOpt = eventRepo.findById(id);
        if (eventOpt.isEmpty()) return null;
        EventDTO dto = new EventDTO();
        BeanUtils.copyProperties(eventOpt.get(), dto);
        dto.setCreatedBy(eventOpt.get().getCreatedBy() != null ? eventOpt.get().getCreatedBy().getEmployeeId() : null);
        dto.setStatus(eventOpt.get().getStatus().name());
        return dto;
    }

    @Override
    public List<EventDTO> getAllEvents() {
        return eventRepo.findAll()
                .stream()
                .map(event -> {
                    EventDTO dto = new EventDTO();
                    BeanUtils.copyProperties(event, dto);
                    dto.setCreatedBy(event.getCreatedBy() != null ? event.getCreatedBy().getEmployeeId() : null);
                    dto.setStatus(event.getStatus().name());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<EventDTO> getEventsByEmployeeId(Integer employeeId) {
        return eventRepo.findByCreatedBy_EmployeeId(employeeId)
                .stream()
                .map(event -> {
                    EventDTO dto = new EventDTO();
                    BeanUtils.copyProperties(event, dto);
                    dto.setCreatedBy(event.getCreatedBy() != null ? event.getCreatedBy().getEmployeeId() : null);
                    dto.setStatus(event.getStatus().name());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public EventDTO approveEvent(Long id) {
        EventEntity event = eventRepo.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
        event.setStatus(EventStatus.APPROVED);
        EventEntity saved = eventRepo.save(event);
        EventDTO dto = new EventDTO();
        BeanUtils.copyProperties(saved, dto);
        dto.setCreatedBy(saved.getCreatedBy() != null ? saved.getCreatedBy().getEmployeeId() : null);
        dto.setStatus(saved.getStatus().name());
        return dto;
    }

    @Override
    public EventDTO rejectEvent(Long id) {
        EventEntity event = eventRepo.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
        event.setStatus(EventStatus.REJECTED);
        EventEntity saved = eventRepo.save(event);
        EventDTO dto = new EventDTO();
        BeanUtils.copyProperties(saved, dto);
        dto.setCreatedBy(saved.getCreatedBy() != null ? saved.getCreatedBy().getEmployeeId() : null);
        dto.setStatus(saved.getStatus().name());
        return dto;
    }

    @Override
    public List<EventDTO> getEventsByUserId(Long userId) {
        return eventRepo.findByUser_Id(userId)
            .stream()
            .map(this::toDTO)
            .collect(java.util.stream.Collectors.toList());
    }

    private EventDTO toDTO(EventEntity entity) {
        EventDTO dto = new EventDTO();
        BeanUtils.copyProperties(entity, dto);
        if (entity.getFileData() != null) {
            dto.setFileData(Base64.getEncoder().encodeToString(entity.getFileData()));
        }
        if (entity.getCreatedBy() != null) {
            dto.setCreatedBy(entity.getCreatedBy().getEmployeeId());
            dto.setCreatedByUserId(entity.getCreatedBy().getUser() != null ? entity.getCreatedBy().getUser().getId() : null);
            dto.setCreatedByFirstName(entity.getCreatedBy().getFirstName());
        }
        if (entity.getUser() != null) {
            dto.setUserId(entity.getUser().getId());
        }
        dto.setStatus(entity.getStatus() != null ? entity.getStatus().name() : null);
        return dto;
    }
} 
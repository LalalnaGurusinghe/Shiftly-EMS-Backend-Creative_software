package com.EMS.Employee.Management.System.service.impl;

import com.EMS.Employee.Management.System.dto.EventDTO;
import com.EMS.Employee.Management.System.entity.EventEntity;
import com.EMS.Employee.Management.System.repo.EventRepo;
import com.EMS.Employee.Management.System.service.EventService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {
    private final EventRepo eventRepo;

    public EventServiceImpl(EventRepo eventRepo) {
        this.eventRepo = eventRepo;
    }

    @Override
    public EventDTO addEvent(@Valid EventDTO eventDTO) {
        EventEntity eventEntity = new EventEntity();
        BeanUtils.copyProperties(eventDTO, eventEntity);
        EventEntity savedEntity = eventRepo.save(eventEntity);
        eventDTO.setId(savedEntity.getId());
        return eventDTO;
    }

    @Override
    public List<EventDTO> getAllEvent() {
        return eventRepo.findAll().stream()
                .map(entity -> {
                    EventDTO eventDTO = new EventDTO();
                    BeanUtils.copyProperties(entity, eventDTO);
                    return eventDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<EventDTO> getEventById(Long id) {
        Optional<EventEntity> event = eventRepo.findById(id);
        if (event.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        EventDTO eventDTO = new EventDTO();
        BeanUtils.copyProperties(event.get(), eventDTO);
        return ResponseEntity.ok(eventDTO);
    }

    @Override
    public ResponseEntity<EventDTO> deleteEventById(Long id) {
        Optional<EventEntity> event = eventRepo.findById(id);
        if (event.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        EventDTO eventDTO = new EventDTO();
        BeanUtils.copyProperties(event.get(), eventDTO);
        eventRepo.deleteById(id);
        return ResponseEntity.ok(eventDTO);
    }

    @Override
    public ResponseEntity<EventDTO> updateEventById(Long id, @Valid EventDTO eventDTO) {
        Optional<EventEntity> event = eventRepo.findById(id);
        if (event.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        EventEntity eventEntity = event.get();
        if (eventDTO.getTitle() != null) eventEntity.setTitle(eventDTO.getTitle());
        if (eventDTO.getFormUrl() != null) eventEntity.setFormUrl(eventDTO.getFormUrl());
        if (eventDTO.getResponseUrl() != null) eventEntity.setResponseUrl(eventDTO.getResponseUrl());
        if (eventDTO.getAudience() != null) eventEntity.setAudience(eventDTO.getAudience());
        if (eventDTO.getEventType() != null) eventEntity.setEventType(eventDTO.getEventType());
        if (eventDTO.getProjects() != null) eventEntity.setProjects(eventDTO.getProjects());
        if (eventDTO.getEnableDateTime() != null) eventEntity.setEnableDateTime(eventDTO.getEnableDateTime());
        if (eventDTO.getExpireDateTime() != null) eventEntity.setExpireDateTime(eventDTO.getExpireDateTime());
        if (eventDTO.getCreatedBy() != null) eventEntity.setCreatedBy(eventDTO.getCreatedBy());
        if (eventDTO.getPhoto() != null) eventEntity.setPhoto(eventDTO.getPhoto());
        eventRepo.save(eventEntity);
        BeanUtils.copyProperties(eventEntity, eventDTO);
        return ResponseEntity.ok(eventDTO);
    }
}
package com.EMS.Employee.Management.System.service.impl;

import com.EMS.Employee.Management.System.entity.EventEntity;
import com.EMS.Employee.Management.System.dto.EventDTO;
import com.EMS.Employee.Management.System.repo.EventRepo;
import com.EMS.Employee.Management.System.service.EventService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {

    @Autowired
    private EventRepo eventRepo;
    @Override
    public EventDTO addEvent(EventDTO eventDTO) {
        EventEntity eventEntity = new EventEntity();
        BeanUtils.copyProperties(eventDTO, eventEntity);
        EventEntity savedEntity = eventRepo.save(eventEntity);
        eventDTO.setId(savedEntity.getId());
        return eventDTO;
    }

    @Override
    public List<EventDTO> getAllEvent() {

        List<EventEntity> eventEntities = eventRepo.findAll();

        List<EventDTO> eventDTOS = eventEntities
                .stream().map(eventEntity -> new EventDTO(
                        eventEntity.getId(),
                        eventEntity.getTitle(),
                        eventEntity.getFormUrl(),
                        eventEntity.getResponseUrl(),
                        eventEntity.getAudience(),
                        eventEntity.getEventType(),
                        eventEntity.getProjects(),
                        eventEntity.getEnableDate(),
                        eventEntity.getExpireDate()


                ))
                .collect(Collectors.toList());


        return eventDTOS;
    }

    @Override
    public ResponseEntity<EventDTO> getEventById(Long id) {
        Optional<EventEntity> event = eventRepo.findById(id);

        if(!event.isPresent()){

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);

        }

        EventEntity eventEntity =  event.get();

        EventDTO eventDTO = new EventDTO();
        BeanUtils.copyProperties(eventEntity , eventDTO);
        return ResponseEntity.ok(eventDTO);
    }

    @Override
    public ResponseEntity<EventDTO> deleteEventById(Long id) {
        Optional<EventEntity> event = eventRepo.findById(id);
        if(!event.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        EventEntity eventEntity = event.get();

        EventDTO eventDTO = new EventDTO();
        BeanUtils.copyProperties(eventEntity , eventDTO);
        eventRepo.deleteById(id);

        return ResponseEntity.ok(eventDTO);
    }

    @Override
    public ResponseEntity<EventDTO> updateEventById(Long id, EventDTO eventDTO) {
        Optional<EventEntity> event = eventRepo.findById(id);

        if (!event.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        EventEntity eventEntity = event.get();

        if (eventDTO.getTitle() != null) {
            eventEntity.setTitle(eventDTO.getTitle());
        }
        if (eventDTO.getFormUrl() != null) {
            eventEntity.setFormUrl(eventDTO.getFormUrl());
        }
        if (eventDTO.getResponseUrl() != null) {
            eventEntity.setResponseUrl(eventDTO.getResponseUrl());
        }
        if (eventDTO.getAudience() != null) {
            eventEntity.setAudience(eventDTO.getAudience());
        }
        if (eventDTO.getEventType() != null) {
            eventEntity.setEventType(eventDTO.getEventType());
        }
        if (eventDTO.getProjects() != null) {
            eventEntity.setProjects(eventDTO.getProjects());
        }
        if (eventDTO.getEnableDate() != null) {
            eventEntity.setEnableDate(eventDTO.getEnableDate());
        }
        if (eventDTO.getExpireDate() != null) {
            eventEntity.setExpireDate(eventDTO.getExpireDate());
        }

        eventRepo.save(eventEntity);
        BeanUtils.copyProperties(eventEntity, eventDTO);

        return ResponseEntity.ok(eventDTO);
    }

}

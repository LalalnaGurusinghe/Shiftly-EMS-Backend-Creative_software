package com.EMS.Employee.Management.System.service;

import com.EMS.Employee.Management.System.dto.EventDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface EventService {
    EventDTO addEvent(EventDTO eventDTO);
    List<EventDTO> getAllEvent();
    ResponseEntity<EventDTO> getEventById(Long id);
    ResponseEntity<EventDTO> deleteEventById(Long id);
    ResponseEntity<EventDTO> updateEventById(Long id, EventDTO eventDTO);
}
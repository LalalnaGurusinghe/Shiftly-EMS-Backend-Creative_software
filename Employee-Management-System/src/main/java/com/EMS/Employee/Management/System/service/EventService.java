package com.EMS.Employee.Management.System.service;

import com.EMS.Employee.Management.System.dto.EventDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface EventService {

    public EventDTO addEvent(EventDTO eventDTO);

    public List<EventDTO> getAllEvent();

    public ResponseEntity<EventDTO> getEventById(Long id);

    public ResponseEntity<EventDTO> deleteEventById(Long id);

    public ResponseEntity<EventDTO> updateEventById(Long id , EventDTO eventDTO);
}

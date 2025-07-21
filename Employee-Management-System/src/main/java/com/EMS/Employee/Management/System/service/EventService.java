package com.EMS.Employee.Management.System.service;

import com.EMS.Employee.Management.System.dto.EventDTO;
import java.util.List;

public interface EventService {
    EventDTO createEvent(EventDTO eventDTO);
    EventDTO updateEvent(Long id, EventDTO eventDTO);
    void deleteEvent(Long id);
    EventDTO getEventById(Long id);
    List<EventDTO> getAllEvents();
    List<EventDTO> getEventsByEmployeeId(Integer employeeId);
    EventDTO approveEvent(Long id);
    EventDTO rejectEvent(Long id);
    List<EventDTO> getEventsByUserId(Long userId);
} 
package com.EMS.Employee.Management.System.service;

import com.EMS.Employee.Management.System.dto.EventDTO;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.util.List;

public interface EventService {
    EventDTO createEvent(String title, String eventType, LocalDate enableDate, LocalDate expireDate, MultipartFile image, String status, String username) throws Exception;
    List<EventDTO> getAllEvents();
    List<EventDTO> getEventsByUserId(Long userId);
    EventDTO updateEvent(Long id, EventDTO eventDTO, String username);
    void deleteEvent(Long id, String username);
    EventDTO getEventById(Long id);
    EventDTO approveEvent(Long id);
    EventDTO rejectEvent(Long id);
}
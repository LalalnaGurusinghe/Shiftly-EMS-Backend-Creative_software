package com.EMS.Employee.Management.System.controller;

import com.EMS.Employee.Management.System.dto.EventDTO;
import com.EMS.Employee.Management.System.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/shiftly/ems/event")
@CrossOrigin
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventDTO> addEvent(
            @Valid @RequestPart("event") EventDTO eventDTO,
            @RequestPart(value = "photo", required = false) MultipartFile photo) throws IOException {
        if (photo != null && !photo.isEmpty()) {
            if (photo.getSize() > 5 * 1024 * 1024) {
                throw new IllegalArgumentException("Image size exceeds 5MB");
            }
            if (!photo.getContentType().startsWith("image/")) {
                throw new IllegalArgumentException("Only image files are allowed");
            }
            eventDTO.setPhoto(photo.getBytes());
        }
        EventDTO savedEvent = eventService.addEvent(eventDTO);
        return ResponseEntity.status(201).body(savedEvent);
    }

    @GetMapping("/all")
    public List<EventDTO> getAllEvent() {
        return eventService.getAllEvent();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable Long id) {
        return eventService.getEventById(id);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventDTO> deleteEventById(@PathVariable Long id) {
        return eventService.deleteEventById(id);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventDTO> updateEventById(@PathVariable Long id, @Valid @RequestBody EventDTO eventDTO) {
        return eventService.updateEventById(id, eventDTO);
    }
}
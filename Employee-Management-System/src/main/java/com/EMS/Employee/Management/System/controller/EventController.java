package com.EMS.Employee.Management.System.controller;

import com.EMS.Employee.Management.System.dto.EventDTO;
import com.EMS.Employee.Management.System.service.EventService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import org.springframework.security.core.Authentication;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/shiftly/ems/events")
@CrossOrigin(origins = "http://localhost:3000")
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    // Admin: View all events
    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    // Admin: Approve event
    @PutMapping("/approve/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventDTO> approveEvent(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.approveEvent(id));
    }

    // Admin: Reject event
    @PutMapping("/reject/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventDTO> rejectEvent(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.rejectEvent(id));
    }

    // Employee: Create event
    @PostMapping(value = "/add", consumes = { "multipart/form-data" })
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<EventDTO> createEvent(
            @RequestParam String title,
            @RequestParam String eventType,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate enableDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expireDate,
            @RequestParam(required = false) MultipartFile image,
            @RequestParam(required = false) String status,
            Authentication authentication) throws Exception {
        EventDTO dto = eventService.createEvent(
                title, eventType, enableDate, expireDate, image, status, authentication.getName());
        return ResponseEntity.ok(dto);
    }

    // Employee: View own events
    @GetMapping("/my")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<EventDTO>> getOwnEvents(Principal principal) {
        List<EventDTO> events = eventService.getOwnEvents(principal.getName());
        return ResponseEntity.ok(events);
    }

    // Employee: Edit own event
    @PutMapping(value = "/update/{id}", consumes = { "multipart/form-data" })
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<EventDTO> updateEvent(
            @PathVariable Long id,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String eventType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate enableDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expireDate,
            @RequestParam(required = false) MultipartFile image,
            @RequestParam(required = false) String status,
            Authentication authentication) throws Exception {
        EventDTO dto = eventService.updateEvent(id, title, eventType, enableDate, expireDate, image, status,
                authentication.getName());
        return ResponseEntity.ok(dto);
    }

    // Employee: Delete own event
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id, Authentication authentication) {
        eventService.deleteEvent(id, authentication.getName());
        return ResponseEntity.ok().build();
    }

    // Get all events by userId
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<EventDTO>> getEventsByUserId(@PathVariable Long userId) {
        List<EventDTO> events = eventService.getEventsByUserId(userId);
        return ResponseEntity.ok(events);
    }
}
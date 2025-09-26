package com.EMS.Employee.Management.System.controller;

import com.EMS.Employee.Management.System.dto.ClaimDTO;
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
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping(value = "/add/{employeeId}", consumes = { "multipart/form-data" })
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<EventDTO> createEvent(
            @PathVariable int employeeId,
            @RequestParam String title,
            @RequestParam String eventType,
            @RequestParam String enableDate,
            @RequestParam String expireDate,
            @RequestParam(required = false) MultipartFile image) throws Exception {
        EventDTO dto = eventService.create(
                employeeId,title, eventType, enableDate, expireDate, image);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("employee/{employeeId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<EventDTO>> getByEmployeeId(@PathVariable int employeeId) {
        List<EventDTO> events = eventService.getByEmployeeId(employeeId);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/admin/{adminUserId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<EventDTO>> getClaimsForAdmin(@PathVariable Long adminUserId) {
        List<EventDTO> events = eventService.getEventsForAdmin(adminUserId);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }


    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/status/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EventDTO> updateStatus(@PathVariable Long id, @RequestParam String status) {
        return ResponseEntity.ok(eventService.updateStatus(id, status));
    }

    // Employee: Edit own event
    @PutMapping(value = "/update/{id}", consumes = { "multipart/form-data" })
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<EventDTO> updateEvent(
            @PathVariable Long id,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String eventType,
            @RequestParam(required = false) String enableDate,
            @RequestParam(required = false) String expireDate,
            @RequestParam(required = false) MultipartFile image) throws Exception {
        EventDTO dto = eventService.updateEvent(id, title, eventType, enableDate, expireDate, image);
        return ResponseEntity.ok(dto);
    }
}
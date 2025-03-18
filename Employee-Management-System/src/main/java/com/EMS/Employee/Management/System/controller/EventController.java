package com.EMS.Employee.Management.System.controller;

import com.EMS.Employee.Management.System.dto.EventDTO;
import com.EMS.Employee.Management.System.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/shiftly/ems/event")
@CrossOrigin
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping("/add")
    public EventDTO addEvent(
            @RequestParam("title") String title,
            @RequestParam("formUrl") String formUrl,
            @RequestParam("responseUrl") String responseUrl,
            @RequestParam("audience") String audience,
            @RequestParam("eventType") String eventType,
            @RequestParam("projects") String projects,
            @RequestParam("enableDateTime") String enableDateTime,
            @RequestParam("expireDateTime") String expireDateTime,
            @RequestParam("createdBy") String createdBy,
            @RequestParam(value = "photo", required = false) MultipartFile photo

    ) throws IOException {

        EventDTO eventDTO = new EventDTO();
        eventDTO.setTitle(title);
        eventDTO.setFormUrl(formUrl);
        eventDTO.setResponseUrl(responseUrl);
        eventDTO.setAudience(audience);
        eventDTO.setEventType(eventType);
        eventDTO.setProjects(projects);
        eventDTO.setEnableDateTime(LocalDateTime.parse(enableDateTime));
        eventDTO.setExpireDateTime(LocalDateTime.parse(expireDateTime));
        eventDTO.setCreatedBy(createdBy);

        // Convert image file to byte array if provided
        if (photo != null && !photo.isEmpty()) {
            eventDTO.setPhoto(photo.getBytes());
        }

        return eventService.addEvent(eventDTO);

    }

    @GetMapping("/all")
    public List<EventDTO> getAllEvent(){
        return eventService.getAllEvent();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEventById(@PathVariable Long id){
        return eventService.getEventById(id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<EventDTO> deleteEventById(@PathVariable Long id){
        return eventService.deleteEventById(id);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<EventDTO> updateEventById(@PathVariable Long id , @RequestBody EventDTO eventDTO){
        return eventService.updateEventById(id , eventDTO);
    }
}

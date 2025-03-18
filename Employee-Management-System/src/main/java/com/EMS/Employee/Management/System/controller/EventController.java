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
@RequestMapping("/event")
@CrossOrigin
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping("/add")
    public EventDTO addEvent(@RequestBody EventDTO eventDTO) {
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

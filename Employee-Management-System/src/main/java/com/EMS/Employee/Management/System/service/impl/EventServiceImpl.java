package com.EMS.Employee.Management.System.service.impl;

import com.EMS.Employee.Management.System.dto.EventDTO;
import com.EMS.Employee.Management.System.entity.EventEntity;
import com.EMS.Employee.Management.System.entity.EventStatus;
import com.EMS.Employee.Management.System.entity.User;
import com.EMS.Employee.Management.System.repo.EventRepo;
import com.EMS.Employee.Management.System.repo.UserRepo;
import com.EMS.Employee.Management.System.service.EventService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {
    private final EventRepo eventRepo;
    private final UserRepo userRepo;

    public EventServiceImpl(EventRepo eventRepo, UserRepo userRepo) {
        this.eventRepo = eventRepo;
        this.userRepo = userRepo;
    }

    @Override
    @Transactional
    public EventDTO createEvent(String title, String eventType, LocalDate enableDate, LocalDate expireDate,
            MultipartFile image, String status, String username) throws Exception {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        EventEntity entity = new EventEntity();
        entity.setTitle(title);
        entity.setEventType(eventType);
        entity.setEnableDate(enableDate);
        entity.setExpireDate(expireDate);
        entity.setStatus(status != null ? EventStatus.valueOf(status) : EventStatus.PENDING);
        entity.setUser(user);

        if (image != null && !image.isEmpty()) {
            String projectRoot = System.getProperty("user.dir");
            String uploadDir = projectRoot + java.io.File.separator + "uploads" + java.io.File.separator + "events"
                    + java.io.File.separator;
            Files.createDirectories(Paths.get(uploadDir));
            String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            entity.setImageUrl("/uploads/events/" + fileName);
        }

        EventEntity saved = eventRepo.save(entity);
        return toDTO(saved);
    }

    @Override
    public List<EventDTO> getAllEvents() {
        return eventRepo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<EventDTO> getEventsByUserId(Long userId) {
        return eventRepo.findByUser_Id(userId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public List<EventDTO> getOwnEvents(String username) {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        return eventRepo.findByUser_Id(user.getId()).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventDTO updateEvent(Long id, String title, String eventType, LocalDate enableDate, LocalDate expireDate,
            MultipartFile image, String status, String username) throws Exception {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        EventEntity entity = eventRepo.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
        if (!entity.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        if (title != null)
            entity.setTitle(title);
        if (eventType != null)
            entity.setEventType(eventType);
        if (enableDate != null)
            entity.setEnableDate(enableDate);
        if (expireDate != null)
            entity.setExpireDate(expireDate);
        if (status != null)
            entity.setStatus(EventStatus.valueOf(status));

        if (image != null && !image.isEmpty()) {
            String projectRoot = System.getProperty("user.dir");
            String uploadDir = projectRoot + java.io.File.separator + "uploads" + java.io.File.separator + "events"
                    + java.io.File.separator;
            Files.createDirectories(Paths.get(uploadDir));
            String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            entity.setImageUrl("/uploads/events/" + fileName);
        }

        EventEntity saved = eventRepo.save(entity);
        return toDTO(saved);
    }

    @Override
    @Transactional
    public void deleteEvent(Long id, String username) {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        EventEntity entity = eventRepo.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
        if (!entity.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        eventRepo.deleteById(id);
    }

    @Override
    public EventDTO getEventById(Long id) {
        EventEntity entity = eventRepo.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
        return toDTO(entity);
    }

    @Override
    @Transactional
    public EventDTO approveEvent(Long id) {
        EventEntity entity = eventRepo.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
        entity.setStatus(EventStatus.APPROVED);
        EventEntity saved = eventRepo.save(entity);
        return toDTO(saved);
    }

    @Override
    @Transactional
    public EventDTO rejectEvent(Long id) {
        EventEntity entity = eventRepo.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
        entity.setStatus(EventStatus.REJECTED);
        EventEntity saved = eventRepo.save(entity);
        return toDTO(saved);
    }

    private EventDTO toDTO(EventEntity entity) {
        EventDTO dto = new EventDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setEventType(entity.getEventType());
        dto.setEnableDate(entity.getEnableDate());
        dto.setExpireDate(entity.getExpireDate());
        dto.setStatus(entity.getStatus() != null ? entity.getStatus().name() : null);
        dto.setImageUrl(entity.getImageUrl());
        dto.setUserId(entity.getUser() != null ? entity.getUser().getId() : null);
        return dto;
    }
}
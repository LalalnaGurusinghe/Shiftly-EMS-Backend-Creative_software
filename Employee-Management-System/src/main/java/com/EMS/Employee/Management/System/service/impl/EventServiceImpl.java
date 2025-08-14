package com.EMS.Employee.Management.System.service.impl;

import com.EMS.Employee.Management.System.dto.ClaimDTO;
import com.EMS.Employee.Management.System.dto.EventDTO;
import com.EMS.Employee.Management.System.entity.*;
import com.EMS.Employee.Management.System.repo.*;
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
    private final EmployeeRepo employeeRepo;
    private final DepartmentRepo departmentRepo;

    public EventServiceImpl(EventRepo eventRepo, EmployeeRepo employeeRepo, DepartmentRepo departmentRepo) {
        this.eventRepo = eventRepo;
        this.employeeRepo = employeeRepo;
        this.departmentRepo = departmentRepo;
    }

    @Override
    @Transactional
    public EventDTO create(int employeeId,String title, String eventType, String enableDate, String expireDate,
            MultipartFile image) throws Exception {
        EmployeeEntity employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        EventEntity entity = new EventEntity();
        entity.setEmployee(employee);
        entity.setTitle(title);
        entity.setEventType(eventType);
        entity.setEnableDate(enableDate);
        entity.setExpireDate(expireDate);
        entity.setStatus(EventStatus.PENDING);

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
    public List<EventDTO> getEventsForAdmin(Long adminUserId) {
        DepartmentEntity department = departmentRepo.findByAdmin_Id(adminUserId)
                .stream().findFirst().orElse(null);
        if (department == null)
            return List.of();
        return eventRepo.findByEmployee_Department_Id(department.getId())
                .stream().map(this::toDTO).toList();
    }

    @Override
    public List<EventDTO> getAllEvents() {
        return eventRepo.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }
    @Override
    public List<EventDTO> getByEmployeeId(int employeeId) {
        return eventRepo.findByEmployee_EmployeeId(employeeId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteEvent(Long id) {
        EventEntity event  = eventRepo.findById(id).orElseThrow(() -> new
                RuntimeException("Event not found"));
        eventRepo.delete(event);
    }

    @Override
    @Transactional
    public EventDTO updateStatus(Long id, String status) {
        EventEntity event = eventRepo.findById(id).orElseThrow(() -> new
                RuntimeException("Event not found"));
        event.setStatus(EventStatus.valueOf(status));
        EventEntity saved = eventRepo.save(event);
        return toDTO(saved);
    }

    @Override
    @Transactional
    public EventDTO updateEvent(Long id, String title, String eventType, String enableDate, String expireDate,
            MultipartFile image) throws Exception {
        EventEntity entity = eventRepo.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));

        if (title != null)
            entity.setTitle(title);
        if (eventType != null)
            entity.setEventType(eventType);
        if (enableDate != null)
            entity.setEnableDate(enableDate);
        if (expireDate != null)
            entity.setExpireDate(expireDate);

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

    private EventDTO toDTO(EventEntity entity) {
        EventDTO dto = new EventDTO();
        if (entity.getEmployee() != null) {
            dto.setEmployeeId(entity.getEmployee().getEmployeeId());
            dto.setDepartmentName(
                    entity.getEmployee().getDepartment() != null ? entity.getEmployee().getDepartment().getName()
                            : null);
            dto.setEmployeeName(entity.getEmployee().getFirstName() != null ? entity.getEmployee().getFirstName() + " " + entity.getEmployee().getLastName() : null);
        }
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setEventType(entity.getEventType());
        dto.setEnableDate(entity.getEnableDate());
        dto.setExpireDate(entity.getExpireDate());
        dto.setStatus(entity.getStatus().name());
        dto.setImageUrl(entity.getImageUrl());
        return dto;
    }
}
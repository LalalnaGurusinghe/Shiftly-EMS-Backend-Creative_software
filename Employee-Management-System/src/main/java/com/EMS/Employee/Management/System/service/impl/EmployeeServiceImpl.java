package com.EMS.Employee.Management.System.service.impl;

import com.EMS.Employee.Management.System.dto.EmployeeDTO;
import com.EMS.Employee.Management.System.entity.EmployeeEntity;
import com.EMS.Employee.Management.System.entity.User;
import com.EMS.Employee.Management.System.repo.EmployeeRepo;
import com.EMS.Employee.Management.System.service.EmployeeService;
import com.EMS.Employee.Management.System.repo.DepartmentRepo;
import com.EMS.Employee.Management.System.entity.DepartmentEntity;
import com.EMS.Employee.Management.System.repo.TeamRepo;
import com.EMS.Employee.Management.System.entity.TeamEntity;
import com.EMS.Employee.Management.System.repo.UserRepo;
import jakarta.validation.ValidationException;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepo employeeRepo;
    private final DepartmentRepo departmentRepo;
    private final TeamRepo teamRepo;
    private final UserRepo userRepo;

    public EmployeeServiceImpl(EmployeeRepo employeeRepo, DepartmentRepo departmentRepo, TeamRepo teamRepo, UserRepo userRepo) {
        this.employeeRepo = employeeRepo;
        this.departmentRepo = departmentRepo;
        this.teamRepo = teamRepo;
        this.userRepo = userRepo;
    }

    @Override
    public ResponseEntity<EmployeeDTO> addUser(EmployeeDTO employeeDTO) {
        EmployeeEntity employeeEntity = new EmployeeEntity();

        // Set User reference from userId
        if (employeeDTO.getUserId() != null) {
            User user = userRepo.findById(employeeDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
            employeeEntity.setUser(user);
        } else {
            throw new RuntimeException("UserId is required");
        }

        // Set Department reference from departmentId
        if (employeeDTO.getDepartmentId() != null) {
            DepartmentEntity department = departmentRepo.findById(employeeDTO.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));
            employeeEntity.setDepartment(department);
        } else {
            throw new RuntimeException("DepartmentId is required");
        }

        // Set designation
        employeeEntity.setDesignation(employeeDTO.getDesignation());

        // Set Reporting Person reference from reportingPersonId
        if (employeeDTO.getReportingPersonId() != null) {
            User reportingPerson = userRepo.findById(employeeDTO.getReportingPersonId())
                .orElseThrow(() -> new RuntimeException("Reporting person not found"));
            employeeEntity.setReportingPerson(reportingPerson);
        }

        // Set reporting person email
        employeeEntity.setReportingPersonEmail(employeeDTO.getReportingPersonEmail());

        // Save the employee record
        EmployeeEntity savedEntity = employeeRepo.save(employeeEntity);

        // Prepare response DTO
        EmployeeDTO responseDTO = new EmployeeDTO();
        responseDTO.setEmployeeId(savedEntity.getEmployeeId());
        responseDTO.setUserId(savedEntity.getUser().getId());
        responseDTO.setDesignation(savedEntity.getDesignation());
        responseDTO.setDepartmentId(savedEntity.getDepartment().getId());
        responseDTO.setDepartment(savedEntity.getDepartment().getName());
        responseDTO.setReportingPersonId(savedEntity.getReportingPerson() != null ? savedEntity.getReportingPerson().getId() : null);
        responseDTO.setReportingPersonEmail(savedEntity.getReportingPersonEmail());

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @Override
    public List<EmployeeDTO> getAll() {
        return employeeRepo.findAll()
                .stream()
                .map(entity -> {
                    EmployeeDTO dto = new EmployeeDTO();
                    BeanUtils.copyProperties(entity, dto);
                    dto.setUserId(entity.getUser() != null ? entity.getUser().getId() : null);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<EmployeeDTO> getUserById(int id) {
        EmployeeEntity entity = employeeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        EmployeeDTO dto = new EmployeeDTO();
        BeanUtils.copyProperties(entity, dto);
        dto.setUserId(entity.getUser() != null ? entity.getUser().getId() : null);
        return ResponseEntity.ok(dto);
    }

    // @Override
    // public List<EmployeeDTO> getEmployeesByDepartment(int departmentId) {
    //     return employeeRepo.findByDepartment(departmentId).stream()
    //         .map(entity -> {
    //             EmployeeDTO dto = new EmployeeDTO();
    //             BeanUtils.copyProperties(entity, dto);
    //             dto.setUserId(entity.getUser() != null ? entity.getUser().getId() : null);
    //             return dto;
    //         })
    //         .collect(Collectors.toList());
    // }

    @Override
    public String getEmployeeNameById(int id) {
        EmployeeEntity employee = employeeRepo.findById(id).orElse(null);
        if (employee == null) return null;
        String firstName = employee.getFirstName() != null ? employee.getFirstName() : "";
        String lastName = employee.getLastName() != null ? employee.getLastName() : "";
        return (firstName + " " + lastName).trim();
    }

    @Transactional
    @Override
    public ResponseEntity<EmployeeDTO> deleteUserById(int id) {
        Optional<EmployeeEntity> optionalEmployee = employeeRepo.findById(id);
        if (optionalEmployee.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        EmployeeDTO employeeDTO = new EmployeeDTO();
        BeanUtils.copyProperties(optionalEmployee.get(), employeeDTO);
        employeeRepo.deleteById(id);
        return ResponseEntity.ok(employeeDTO);
    }

    @Override
    public ResponseEntity<EmployeeDTO> updateUserById(int id, EmployeeDTO employeeDTO) {
        EmployeeEntity entity = employeeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (employeeDTO.getFirstName() != null) entity.setFirstName(employeeDTO.getFirstName());
        if (employeeDTO.getLastName() != null) entity.setLastName(employeeDTO.getLastName());
        if (employeeDTO.getGender() != null) entity.setGender(employeeDTO.getGender());
        if (employeeDTO.getDob() != null) entity.setDob(employeeDTO.getDob());
        if (employeeDTO.getLocation() != null) entity.setLocation(employeeDTO.getLocation());
        if (employeeDTO.getSkills() != null) entity.setSkills(employeeDTO.getSkills());
        if (employeeDTO.getEducation() != null) entity.setEducation(employeeDTO.getEducation());
        if (employeeDTO.getExperience() != null) entity.setExperience(employeeDTO.getExperience());
        if (employeeDTO.getTeamName() != null) entity.setTeamName(employeeDTO.getTeamName());
        // Set User reference from userId if provided
        if (employeeDTO.getUserId() != null) {
            User user = userRepo.findById(employeeDTO.getUserId()).orElse(null);
            entity.setUser(user);
        }
        employeeRepo.save(entity);
        EmployeeDTO updatedDTO = toDTO(entity);
        return ResponseEntity.ok(updatedDTO);
    }

    private EmployeeDTO toDTO(EmployeeEntity entity) {
        EmployeeDTO dto = new EmployeeDTO();
        BeanUtils.copyProperties(entity, dto, "employeeId", "user", "username");
        // User info
        if (entity.getUser() != null) {
            User user = userRepo.findById(entity.getUser().getId()).orElse(null);
            if (user != null) {
                dto.setUserId(user.getId());
            }
        }
         if (dto.getDepartmentId() != null) {
            DepartmentEntity department = departmentRepo.findById(dto.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));
            dto.setDepartment(department.getName());
        } else {
            throw new RuntimeException("DepartmentId is required");
        }
        dto.setDepartment(entity.getDepartment().getName());
        dto.setSkills(entity.getSkills());
        dto.setEducation(entity.getEducation());
        dto.setExperience(entity.getExperience());
        dto.setTeamName(entity.getTeamName());
        return dto;
    }

    // @Override
    // public ResponseEntity<EmployeeDTO> updateProfileFields(int id, EmployeeDTO employeeDTO) {
    //     EmployeeEntity entity = employeeRepo.findById(id)
    //             .orElseThrow(() -> new RuntimeException("Employee not found"));

    //     // Example: update only profile fields (customize as needed)
    //     if (employeeDTO.getFirstName() != null) entity.setFirstName(employeeDTO.getFirstName());
    //     if (employeeDTO.getLastName() != null) entity.setLastName(employeeDTO.getLastName());
    //     if (employeeDTO.getGender() != null) entity.setGender(employeeDTO.getGender());
    //     if (employeeDTO.getDob() != null) entity.setDob(employeeDTO.getDob());
    //     if (employeeDTO.getLocation() != null) entity.setLocation(employeeDTO.getLocation());

    //     employeeRepo.save(entity);
    //     EmployeeDTO updatedDTO = toDTO(entity);
    //     return ResponseEntity.ok(updatedDTO);
    // }
}
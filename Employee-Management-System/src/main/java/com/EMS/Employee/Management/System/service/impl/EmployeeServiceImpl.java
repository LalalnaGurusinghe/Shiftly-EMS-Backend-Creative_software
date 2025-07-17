package com.EMS.Employee.Management.System.service.impl;

import com.EMS.Employee.Management.System.dto.EmployeeDTO;
import com.EMS.Employee.Management.System.entity.EmployeeEntity;
import com.EMS.Employee.Management.System.entity.User;
import com.EMS.Employee.Management.System.repo.EmployeeRepo;
import com.EMS.Employee.Management.System.service.EmployeeService;
import com.EMS.Employee.Management.System.repo.DepartmentRepo;
import com.EMS.Employee.Management.System.entity.DepartmentEntity;
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

    public EmployeeServiceImpl(EmployeeRepo employeeRepo, DepartmentRepo departmentRepo) {
        this.employeeRepo = employeeRepo;
        this.departmentRepo = departmentRepo;
    }

    @Override
    public ResponseEntity<EmployeeDTO> addUser(EmployeeDTO employeeDTO) {
        EmployeeEntity employeeEntity = new EmployeeEntity();
        BeanUtils.copyProperties(employeeDTO, employeeEntity);
        // Set User reference from userId
        if (employeeDTO.getUserId() != null) {
            User user = new User();
            user.setId(employeeDTO.getUserId());
            employeeEntity.setUser(user);
        }
        EmployeeEntity savedEntity = employeeRepo.save(employeeEntity);
        employeeDTO.setEmployeeId(savedEntity.getEmployeeId());
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeDTO);
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
        if (employeeDTO.getDesignation() != null) entity.setDesignation(employeeDTO.getDesignation());
        if (employeeDTO.getDepartmentId() != null) {
            DepartmentEntity department = departmentRepo.findById(employeeDTO.getDepartmentId())
                .orElse(null);
            entity.setDepartment(department);
        }
        if (employeeDTO.getReportingPersonId() != null) {
            EmployeeEntity reportingPerson = employeeRepo.findById(employeeDTO.getReportingPersonId())
                .orElse(null);
            entity.setReportingPerson(reportingPerson);
        }
        if (employeeDTO.getSkills() != null) {
            List<com.EMS.Employee.Management.System.entity.SkillEntity> skillEntities = employeeDTO.getSkills().stream().map(dto -> {
                com.EMS.Employee.Management.System.entity.SkillEntity skill = new com.EMS.Employee.Management.System.entity.SkillEntity();
                skill.setId(dto.getId());
                skill.setName(dto.getName());
                skill.setEmployee(entity);
                return skill;
            }).collect(Collectors.toList());
            entity.setSkills(skillEntities);
        }
        if (employeeDTO.getEducation() != null) {
            List<com.EMS.Employee.Management.System.entity.EducationEntity> educationEntities = employeeDTO.getEducation().stream().map(dto -> {
                com.EMS.Employee.Management.System.entity.EducationEntity education = new com.EMS.Employee.Management.System.entity.EducationEntity();
                education.setId(dto.getId());
                education.setName(dto.getName());
                education.setEmployee(entity);
                return education;
            }).collect(Collectors.toList());
            entity.setEducation(educationEntities);
        }
        // Set User reference from userId if provided
        if (employeeDTO.getUserId() != null) {
            User user = new User();
            user.setId(employeeDTO.getUserId());
            entity.setUser(user);
        }
        employeeRepo.save(entity);
        EmployeeDTO updatedDTO = new EmployeeDTO();
        BeanUtils.copyProperties(entity, updatedDTO);
        updatedDTO.setUserId(entity.getUser() != null ? entity.getUser().getId() : null);
        return ResponseEntity.ok(updatedDTO);
    }
}
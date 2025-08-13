package com.EMS.Employee.Management.System.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.EMS.Employee.Management.System.dto.EmployeeDTO;
import com.EMS.Employee.Management.System.entity.DepartmentEntity;
import com.EMS.Employee.Management.System.entity.DesignationEntity;
import com.EMS.Employee.Management.System.entity.EmployeeEntity;
import com.EMS.Employee.Management.System.entity.User;
import com.EMS.Employee.Management.System.repo.DepartmentRepo;
import com.EMS.Employee.Management.System.repo.DesignationRepo;
import com.EMS.Employee.Management.System.repo.EmployeeRepo;
import com.EMS.Employee.Management.System.repo.TeamRepo;
import com.EMS.Employee.Management.System.repo.UserRepo;
import com.EMS.Employee.Management.System.service.DepartmentService;
import com.EMS.Employee.Management.System.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepo employeeRepo;
    private final DepartmentRepo departmentRepo;
    private final TeamRepo teamRepo;
    private final UserRepo userRepo;
    private final DepartmentService departmentService;
    private final DesignationRepo designationRepo;

    public EmployeeServiceImpl(EmployeeRepo employeeRepo, DepartmentRepo departmentRepo, TeamRepo teamRepo,
            UserRepo userRepo, DepartmentService departmentService, DesignationRepo designationRepo) {
        this.employeeRepo = employeeRepo;
        this.departmentRepo = departmentRepo;
        this.teamRepo = teamRepo;
        this.userRepo = userRepo;
        this.departmentService = departmentService;
        this.designationRepo = designationRepo;
    }

    @Override
    @Transactional
    public ResponseEntity<EmployeeDTO> addEmployee(Long userId, EmployeeDTO employeeDTO) {
        if (employeeRepo.findByUser_Id(userId).isPresent()) {
            return updateEmployee(userId, employeeDTO);
        }

        employeeDTO.setUserId(userId);

        EmployeeEntity employeeEntity = new EmployeeEntity();

        User user = userRepo.findById(employeeDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        employeeEntity.setUser(user);

        if (employeeDTO.getDepartmentId() != null) {
            DepartmentEntity department = departmentRepo.findById(employeeDTO.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found"));
            employeeEntity.setDepartment(department);
        } else {
            throw new RuntimeException("DepartmentId is required");
        }

        if (employeeDTO.getDesignationId() != null) {
            DesignationEntity designation = designationRepo.findById(employeeDTO.getDesignationId())
                    .orElseThrow(() -> new RuntimeException("Designation not found"));
            employeeEntity.setDesignation(designation);
        } else {
            throw new RuntimeException("DesignationId is required");
        }

        if (employeeDTO.getReportingPersonId() != null) {
            User reportingPerson = userRepo.findById(employeeDTO.getReportingPersonId())
                    .orElseThrow(() -> new RuntimeException("Reporting person not found"));
            employeeEntity.setReportPerson(reportingPerson);
        } else {
            throw new RuntimeException("ReportingPersonId is required");
        }

        EmployeeEntity saved = employeeRepo.save(employeeEntity);
        return new ResponseEntity<>(toDTO(saved), HttpStatus.CREATED);
    }

    @Override
    public List<EmployeeDTO> getEmployeesByDepartment(Long departmentId) {
    return employeeRepo.findByDepartment_Id(departmentId).stream()
     .map(this::toDTO)
    .collect(Collectors.toList());
    }

    // @Override
    // public List<EmployeeDTO> getAll() {
    //     return employeeRepo.findAll()
    //             .stream()
    //             .map(this::toDTO)
    //             .collect(Collectors.toList());
    // }

    // @Override
    // public ResponseEntity<EmployeeDTO> getUserById(int id) {
    //     EmployeeEntity entity = employeeRepo.findById(id)
    //             .orElseThrow(() -> new RuntimeException("Employee not found"));
    //     return ResponseEntity.ok(toDTO(entity));
    // }


    // @Override
    // public String getEmployeeNameById(int id) {
    //     EmployeeEntity employee = employeeRepo.findById(id).orElse(null);
    //     if (employee == null)
    //         return null;
    //     String firstName = employee.getFirstName() != null ? employee.getFirstName() : "";
    //     String lastName = employee.getLastName() != null ? employee.getLastName() : "";
    //     return (firstName + " " + lastName).trim();
    // }

    @Transactional
    @Override
    public void deleteEmployeeByUserId(Long id) {
         employeeRepo.deleteByUser_Id(id);
    }

    // @Override
    // public ResponseEntity<EmployeeDTO> updateUserById(int id, EmployeeDTO employeeDTO) {
    //     EmployeeEntity entity = employeeRepo.findById(id)
    //             .orElseThrow(() -> new RuntimeException("Employee not found"));

    //     if (employeeDTO.getFirstName() != null)
    //         entity.setFirstName(employeeDTO.getFirstName());
    //     if (employeeDTO.getLastName() != null)
    //         entity.setLastName(employeeDTO.getLastName());
    //     if (employeeDTO.getGender() != null)
    //         entity.setGender(employeeDTO.getGender());
    //     if (employeeDTO.getDob() != null)
    //         entity.setDob(employeeDTO.getDob());
    //     if (employeeDTO.getLocation() != null)
    //         entity.setLocation(employeeDTO.getLocation());
    //     if (employeeDTO.getSkills() != null)
    //         entity.setSkills(employeeDTO.getSkills());
    //     if (employeeDTO.getEducation() != null)
    //         entity.setEducation(employeeDTO.getEducation());
    //     if (employeeDTO.getExperience() != null)
    //         entity.setExperience(employeeDTO.getExperience());
    //     if (employeeDTO.getTeamName() != null)
    //         entity.setTeamName(employeeDTO.getTeamName());
    //     // Set User reference from userId if provided
    //     if (employeeDTO.getUserId() != null) {
    //         User user = userRepo.findById(employeeDTO.getUserId()).orElse(null);
    //         entity.setUser(user);
    //     }
    //     employeeRepo.save(entity);
    //     EmployeeDTO updatedDTO = toDTO(entity);
    //     return ResponseEntity.ok(updatedDTO);
    // }

    private EmployeeDTO toDTO(EmployeeEntity entity) {
        EmployeeDTO dto = new EmployeeDTO();

        if (entity.getEmployeeId() != 0) {
            dto.setEmployeeId(entity.getEmployeeId());
        }
        if (entity.getUser() != null) {
            dto.setUserId(entity.getUser().getId());
        }
        if (entity.getDepartment() != null) {
            dto.setDepartmentId(entity.getDepartment().getId());
            dto.setDepartment(entity.getDepartment().getName());
        }
        if (entity.getDesignation() != null) {
            dto.setDesignationId(entity.getDesignation().getId());
            dto.setDesignationName(entity.getDesignation().getName());
        }
        if (entity.getReportPerson() != null) {
            dto.setReportingPersonId(entity.getReportPerson().getId());
            dto.setReportingPerson(entity.getReportPerson().getUsername());
            dto.setReportingPersonEmail(entity.getReportPerson().getEmail());
        } else {
            dto.setReportingPersonId(null);
            dto.setReportingPerson(null);
            dto.setReportingPersonEmail(null);
        }
        if(entity.getFirstName() !=null){
            dto.setFirstName(entity.getFirstName());
        }
        if(entity.getLastName() !=null){
            dto.setLastName(entity.getLastName());
        }
        if(entity.getFirstName() !=null && entity.getLastName() !=null){
            dto.setFullName(entity.getFirstName() + " " + entity.getLastName());
        }
        if(entity.getGender() !=null){
            dto.setGender(entity.getGender());
        }
        if(entity.getDob() !=null){
            dto.setDob(entity.getDob());
        }
        if(entity.getLocation() !=null){
            dto.setLocation(entity.getLocation());
        }
        if(entity.getSkills() !=null){
            dto.setSkills(entity.getSkills());
        }
        if(entity.getEducation() !=null){
            dto.setEducation(entity.getEducation());
        }
        if(entity.getExperience() !=null){
            dto.setExperience(entity.getExperience());
        }
        if(entity.getTeamName() !=null){
            dto.setTeamName(entity.getTeamName());
        }

        return dto;
    }

    // @Override
    // public ResponseEntity<EmployeeDTO> updateProfileFields(int id, EmployeeDTO
    // employeeDTO) {
    // EmployeeEntity entity = employeeRepo.findById(id)
    // .orElseThrow(() -> new RuntimeException("Employee not found"));

    // // Example: update only profile fields (customize as needed)
    // if (employeeDTO.getFirstName() != null)
    // entity.setFirstName(employeeDTO.getFirstName());
    // if (employeeDTO.getLastName() != null)
    // entity.setLastName(employeeDTO.getLastName());
    // if (employeeDTO.getGender() != null)
    // entity.setGender(employeeDTO.getGender());
    // if (employeeDTO.getDob() != null) entity.setDob(employeeDTO.getDob());
    // if (employeeDTO.getLocation() != null)
    // entity.setLocation(employeeDTO.getLocation());

    // employeeRepo.save(entity);
    // EmployeeDTO updatedDTO = toDTO(entity);
    // return ResponseEntity.ok(updatedDTO);
    // }

    @Override
    public EmployeeDTO getEmployeeByUserId(Long userId) {
        return employeeRepo.findAll().stream()
            .filter(emp -> emp.getUser() != null && emp.getUser().getId().equals(userId))
            .findFirst()
            .map(this::toDTO)
            .orElse(null);
    }

    // Add this new method
    @Override
    @Transactional
    public ResponseEntity<EmployeeDTO> updateEmployee(Long userId, EmployeeDTO employeeDTO) {
        EmployeeEntity entity = employeeRepo.findByUser_Id(userId)
                .orElseThrow(() -> new RuntimeException("Employee not found for userId: " + userId));

        if (employeeDTO.getDepartmentId() != null) {
            DepartmentEntity department = departmentRepo.findById(employeeDTO.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found"));
            entity.setDepartment(department);
        }

        if (employeeDTO.getDesignationId() != null) {
            DesignationEntity designation = designationRepo.findById(employeeDTO.getDesignationId())
                    .orElseThrow(() -> new RuntimeException("Designation not found"));
            entity.setDesignation(designation);
        }

        if (employeeDTO.getReportingPersonId() != null) {
            User reportingPerson = userRepo.findById(employeeDTO.getReportingPersonId())
                    .orElseThrow(() -> new RuntimeException("Reporting person not found"));
            entity.setReportPerson(reportingPerson);
        }

        EmployeeEntity saved = employeeRepo.save(entity);
        return ResponseEntity.ok(toDTO(saved));
    }
}
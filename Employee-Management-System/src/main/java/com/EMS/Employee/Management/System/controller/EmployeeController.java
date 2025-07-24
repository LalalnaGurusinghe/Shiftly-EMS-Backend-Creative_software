package com.EMS.Employee.Management.System.controller;

import com.EMS.Employee.Management.System.dto.EmployeeDTO;
import com.EMS.Employee.Management.System.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.security.core.Authentication;
import com.EMS.Employee.Management.System.repo.UserRepo;
import com.EMS.Employee.Management.System.repo.EmployeeRepo;
import com.EMS.Employee.Management.System.entity.EmployeeEntity;
import com.EMS.Employee.Management.System.entity.User;
import org.springframework.beans.BeanUtils;
import com.EMS.Employee.Management.System.entity.DepartmentEntity;
import com.EMS.Employee.Management.System.repo.DepartmentRepo;

@RestController
@RequestMapping("/api/v1/shiftly/ems/employee")
@CrossOrigin(origins = "http://localhost:3000")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final UserRepo userRepo;
    private final EmployeeRepo employeeRepo;
    private final DepartmentRepo departmentRepo;

    public EmployeeController(EmployeeService employeeService, UserRepo userRepo, EmployeeRepo employeeRepo, DepartmentRepo departmentRepo) {
        this.employeeService = employeeService;
        this.userRepo = userRepo;
        this.employeeRepo = employeeRepo;
        this.departmentRepo = departmentRepo;
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeDTO> addEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
        return employeeService.addUser(employeeDTO);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable int id) {
        return employeeService.getUserById(id);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeDTO> deleteEmployeeById(@PathVariable int id) {
        return employeeService.deleteUserById(id);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeDTO> updateEmployeeById(@PathVariable int id, @Valid @RequestBody EmployeeDTO employeeDTO) {
        return employeeService.updateUserById(id, employeeDTO);
    }

    @PutMapping("/self-update")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<EmployeeDTO> updateOwnInfo(@RequestBody EmployeeDTO employeeDTO) {
        // Assume employeeId is set in DTO and userId is validated by security context in real implementation
        return employeeService.updateUserById(employeeDTO.getEmployeeId(), employeeDTO);
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<EmployeeDTO> getProfile(Authentication authentication) {
        String username = authentication.getName();
        User user = userRepo.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
        EmployeeEntity employee = employeeRepo.findByUser_Id(user.getId());
        if (employee == null) {
            return ResponseEntity.notFound().build();
        }
        EmployeeDTO dto = new EmployeeDTO();
        BeanUtils.copyProperties(employee, dto);
        dto.setUserId(user.getId());
        dto.setUsername(user.getUsername());
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/profile")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<EmployeeDTO> updateProfile(
        Authentication authentication,
        @RequestBody EmployeeDTO employeeDTO
    ) {
        String username = authentication.getName();
        User user = userRepo.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
        EmployeeEntity employee = employeeRepo.findByUser_Id(user.getId());
        if (employee == null) {
            employee = new EmployeeEntity();
            employee.setUser(user);
        }
        boolean isAdmin = user.getRoles().contains("ADMIN") || user.getRoles().contains("SUPER_ADMIN");
        BeanUtils.copyProperties(employeeDTO, employee, "employeeId", "user");
        employee.setUser(user);
        EmployeeEntity saved = employeeRepo.save(employee);
        EmployeeDTO dto = new EmployeeDTO();
        BeanUtils.copyProperties(saved, dto);
        dto.setUserId(user.getId());
        dto.setUsername(user.getUsername());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/by-department/{department}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EmployeeDTO>> getEmployeesByDepartment(@PathVariable String department) {
        return ResponseEntity.ok(employeeService.getEmployeesByDepartment(department));
    }

    @GetMapping("/name/{id}")
    @PreAuthorize(" hasRole('USER')")
    public ResponseEntity<?> getEmployeeNameById(@PathVariable int id) {
        String fullName = employeeService.getEmployeeNameById(id);
        if (fullName == null) {
            return ResponseEntity.notFound().build();
        }
        java.util.Map<String, String> result = new java.util.HashMap<>();
        result.put("fullName", fullName);
        return ResponseEntity.ok(result);
    }
}
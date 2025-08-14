package com.EMS.Employee.Management.System.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.EMS.Employee.Management.System.dto.EmployeeDTO;
import com.EMS.Employee.Management.System.entity.EmployeeEntity;
import com.EMS.Employee.Management.System.entity.User;
import com.EMS.Employee.Management.System.repo.EmployeeRepo;
import com.EMS.Employee.Management.System.repo.UserRepo;
import com.EMS.Employee.Management.System.service.EmployeeService;
import com.EMS.Employee.Management.System.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/shiftly/ems/employee")
@CrossOrigin(origins = "http://localhost:3000")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final UserService userService;
    private final UserRepo userRepo;
    private final EmployeeRepo employeeRepo;

    public EmployeeController(EmployeeService employeeService, UserService userService, UserRepo userRepo,
            EmployeeRepo employeeRepo) {
        this.employeeService = employeeService;
        this.userService = userService;
        this.userRepo = userRepo;
        this.employeeRepo = employeeRepo;
    }

    @PostMapping("/add/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeDTO> addEmployee(
            @PathVariable Long userId,
            @Valid @RequestBody EmployeeDTO employeeDTO) {
        return employeeService.addEmployee(userId, employeeDTO);
    }

    @GetMapping("/by-department/{departmentId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<EmployeeDTO>>
    getEmployeesByDepartment(@PathVariable Long departmentId) {
    return
    ResponseEntity.ok(employeeService.getEmployeesByDepartment(departmentId));
    }

    // @GetMapping("/all")
    // @PreAuthorize("hasRole('ADMIN')")
    // public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
    // return ResponseEntity.ok(employeeService.getAll());
    // }

    // @GetMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    // public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable int id) {
    // return employeeService.getUserById(id);
    // }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEmployeeByUserId(@PathVariable Long id) {
        employeeService.deleteEmployeeByUserId(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/update/{id}")
     @PreAuthorize("hasRole('USER')")
     public ResponseEntity<EmployeeDTO> updateEmployeeById(@PathVariable int id,
     @Valid @RequestBody EmployeeDTO employeeDTO) {
     return employeeService.updateEmployeeById(id, employeeDTO);
     }

    // @PutMapping("/self-update")
    // @PreAuthorize("hasRole('USER')")
    // public ResponseEntity<EmployeeDTO> updateOwnInfo(@RequestBody EmployeeDTO
    // employeeDTO) {
    // // Assume employeeId is set in DTO and userId is validated by security
    // context in real implementation
    // return employeeService.updateUserById(employeeDTO.getEmployeeId(),
    // employeeDTO);
    // }

//    @GetMapping("/profile")
//    @PreAuthorize("hasRole('USER')")
//    public ResponseEntity<EmployeeDTO> getProfile(Authentication authentication) {
//        String username = authentication.getName();
//        User user = userRepo.findByUsername(username)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//        Optional<EmployeeEntity> employee = employeeRepo.findByUser_Id(user.getId());
//        if (employee == null) {
//            return ResponseEntity.notFound().build();
//        }
//        EmployeeDTO dto = new EmployeeDTO();
//        BeanUtils.copyProperties(employee, dto);
//        dto.setUserId(user.getId());
//        return ResponseEntity.ok(dto);
//    }

    // @PutMapping("/profile")
    // @PreAuthorize("hasRole('USER')")
    // public ResponseEntity<EmployeeDTO> updateProfile(
    // Authentication authentication,
    // @RequestBody EmployeeDTO employeeDTO
    // ) {
    // String username = authentication.getName();
    // User user = userRepo.findByUsername(username)
    // .orElseThrow(() -> new RuntimeException("User not found"));
    // EmployeeEntity employee = employeeRepo.findByUser_Id(user.getId());
    // if (employee == null) {
    // employee = new EmployeeEntity();
    // employee.setUser(user);
    // }
    // BeanUtils.copyProperties(employeeDTO, employee, "employeeId", "user");
    // employee.setUser(user);
    // EmployeeEntity saved = employeeRepo.save(employee);
    // EmployeeDTO dto = new EmployeeDTO();
    // BeanUtils.copyProperties(saved, dto);
    // dto.setUserId(user.getId());
    // return ResponseEntity.ok(dto);
    // }

    // @GetMapping("/admins-by-department/{department}")
    // @PreAuthorize("hasRole('ADMIN')")
    // public ResponseEntity<AdminUserResponseDTO>
    // getAdminUserByDepartment(@PathVariable int departmentId) {
    // AdminUserResponseDTO adminUser =
    // userService.getAdminUserByDepartment(departmentId);
    // if (adminUser != null) {
    // return ResponseEntity.ok(adminUser);
    // } else {
    // return ResponseEntity.notFound().build();
    // }
    // }

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeDTO> getEmployeeByUserId(@PathVariable Long userId) {
        EmployeeDTO employee = employeeService.getEmployeeByUserId(userId);
        if (employee == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(employee);
    }

    // @GetMapping("/name/{id}")
    // @PreAuthorize(" hasRole('USER')")
    // public ResponseEntity<?> getEmployeeNameById(@PathVariable int id) {
    //     String fullName = employeeService.getEmployeeNameById(id);
    //     if (fullName == null) {
    //         return ResponseEntity.notFound().build();
    //     }
    //     java.util.Map<String, String> result = new java.util.HashMap<>();
    //     result.put("fullName", fullName);
    //     return ResponseEntity.ok(result);
    // }
}
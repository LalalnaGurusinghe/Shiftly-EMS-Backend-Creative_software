package com.EMS.Employee.Management.System.service;

import com.EMS.Employee.Management.System.dto.LoginRequestDTO;
import com.EMS.Employee.Management.System.dto.LoginResponseDTO;
import com.EMS.Employee.Management.System.dto.RegisterRequestDTO;
import com.EMS.Employee.Management.System.dto.UserDTO;
import com.EMS.Employee.Management.System.entity.User;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
    UserDTO registerEmployee(RegisterRequestDTO registerRequestDTO);
    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);
    ResponseEntity<String> logout();
    void sendVerificationEmail(User user, String role);
}
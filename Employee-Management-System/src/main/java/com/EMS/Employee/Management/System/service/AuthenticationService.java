package com.EMS.Employee.Management.System.service;

import com.EMS.Employee.Management.System.dto.LoginRequestDTO;
import com.EMS.Employee.Management.System.dto.LoginResponseDTO;
import com.EMS.Employee.Management.System.dto.RegisterRequestDTO;
import com.EMS.Employee.Management.System.dto.UserDTO;
import com.EMS.Employee.Management.System.entity.User;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {

    /**
     * Registers a new employee user.
     * @param registerRequestDTO The registration request containing user details.
     * @return UserDTO representing the registered user.
     */
    UserDTO registerEmployee(RegisterRequestDTO registerRequestDTO);

    /**
     * Authenticates a user and returns a login response with a JWT token.
     * @param loginRequestDTO The login request containing username and password.
     * @return LoginResponseDTO containing the authentication token.
     */
    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);

    /**
     * Logs out the current user (invalidates the token if applicable).
     * @return ResponseEntity with a success message.
     */
    ResponseEntity<String> logout();

    /**
     * Sends a verification email to the user with the specified role.
     * @param user The user to send the email to.
     * @param role The role assigned to the user.
     */
    void sendVerificationEmail(User user, String role);
}
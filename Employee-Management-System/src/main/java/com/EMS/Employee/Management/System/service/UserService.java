package com.EMS.Employee.Management.System.service;

import com.EMS.Employee.Management.System.dto.ChangePasswordDTO;
import com.EMS.Employee.Management.System.dto.UserDTO;
import com.EMS.Employee.Management.System.entity.User;

import java.util.List;

public interface UserService {
    UserDTO getUserById(Long id);
    User getUserEntityById(Long id);
    UserDTO getUserByUsername(String username);
    List<UserDTO> getAllUsers();
    UserDTO changePassword(Long id, ChangePasswordDTO changePasswordDTO);
    void deleteUser(Long id);
    UserDTO updateUser(Long id, UserDTO userDTO);
    UserDTO verifyAndUpdateUserRole(Long id, String role);
    List<UserDTO> verifyAllUnverifiedEmployees(String role);
    UserDTO verifyAndUpdateUserRoleAndProfile(Long id, String role, String designation, String department);
}
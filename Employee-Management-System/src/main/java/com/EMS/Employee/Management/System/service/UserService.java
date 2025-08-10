package com.EMS.Employee.Management.System.service;

import java.util.List;

import com.EMS.Employee.Management.System.dto.ChangePasswordDTO;
import com.EMS.Employee.Management.System.dto.UserDTO;
import com.EMS.Employee.Management.System.dto.DetailUserDTO;
import com.EMS.Employee.Management.System.entity.User;

public interface UserService {
    List<UserDTO> getAllUnverifiedUsers();
    List<UserDTO> getAllVerifiedUsers();
    UserDTO getUserById(Long id);
    User getUserEntityById(Long id);
    UserDTO getUserByUsername(String username);
    List<UserDTO> getAll();
    List<DetailUserDTO> getAllUsers();
    UserDTO changePassword(Long id, ChangePasswordDTO changePasswordDTO);
    void deleteUser(Long id);
    UserDTO updateUser(Long id, UserDTO userDTO);
    UserDTO updateUserProfile(Long id, String designation, String department, String reportingPerson, String reportingPersonEmail);
    UserDTO verifyAndUpdateUserRole(Long id, String role);
    List<UserDTO> verifyAllUnverifiedEmployees(String role);
    UserDTO verifyAndUpdateUserRoleAndProfile(Long id, String role);
    // AdminUserResponseDTO getAdminUserByDepartment(String department);
    List<DetailUserDTO> getAllAdmins();
    List<UserDTO> getAllAdminsWithoutDepartment();
}
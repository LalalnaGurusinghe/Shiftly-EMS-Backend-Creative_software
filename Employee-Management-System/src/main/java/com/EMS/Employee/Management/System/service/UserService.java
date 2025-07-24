package com.EMS.Employee.Management.System.service;

import java.util.List;

import com.EMS.Employee.Management.System.dto.ChangePasswordDTO;
import com.EMS.Employee.Management.System.dto.UserDTO;
import com.EMS.Employee.Management.System.dto.AdminUserResponseDTO;
import com.EMS.Employee.Management.System.entity.User;

public interface UserService {
    UserDTO getUserById(Long id);
    User getUserEntityById(Long id);
    UserDTO getUserByUsername(String username);
    List<UserDTO> getAllUsers();
    UserDTO changePassword(Long id, ChangePasswordDTO changePasswordDTO);
    void deleteUser(Long id);
    UserDTO updateUser(Long id, UserDTO userDTO);
    UserDTO updateUserProfile(Long id, String designation, String department, String reportingPerson, String reportingPersonEmail);
    UserDTO verifyAndUpdateUserRole(Long id, String role);
    List<UserDTO> verifyAllUnverifiedEmployees(String role);
    UserDTO verifyAndUpdateUserRoleAndProfile(Long id, String role, String designation, String department, String reportingPerson, String reportingPersonEmail);
    List<Object[]> getUsernameAndDesignationByDepartment(String department);
    AdminUserResponseDTO getAdminUserByDepartment(String department);
}
package com.EMS.Employee.Management.System.service;

import com.EMS.Employee.Management.System.dto.AdminUserDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AdminUserService {

    ResponseEntity<AdminUserDTO> addUser(AdminUserDTO adminUserDTO);

    List<AdminUserDTO> getAll();

    ResponseEntity<AdminUserDTO> getUserById(int id);

    ResponseEntity<AdminUserDTO> deleteUserById(int id);

    ResponseEntity<AdminUserDTO> updateUserById(int id, AdminUserDTO adminUserDTO);
}
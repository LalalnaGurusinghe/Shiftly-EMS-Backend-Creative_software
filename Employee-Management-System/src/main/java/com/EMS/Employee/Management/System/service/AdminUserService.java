package com.EMS.Employee.Management.System.service;

import com.EMS.Employee.Management.System.dto.AdminUserDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface AdminUserService {

    public ResponseEntity<AdminUserDTO> addUser(AdminUserDTO adminUserDTO);

    public List<AdminUserDTO> getAll();

    public ResponseEntity<AdminUserDTO> getUserById(int id);

    public ResponseEntity<AdminUserDTO> deleteUserById(int id);

    public ResponseEntity<AdminUserDTO> updateUserById(int id , AdminUserDTO adminUserDTO);
}

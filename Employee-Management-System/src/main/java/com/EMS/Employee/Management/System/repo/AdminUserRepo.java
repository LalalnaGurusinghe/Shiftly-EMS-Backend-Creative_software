package com.EMS.Employee.Management.System.repo;

import com.EMS.Employee.Management.System.entity.AdminUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminUserRepo extends JpaRepository<AdminUserEntity, Integer> {
    boolean existsByEmail(String email);
    boolean existsByEpfNo(int epfNo);
}
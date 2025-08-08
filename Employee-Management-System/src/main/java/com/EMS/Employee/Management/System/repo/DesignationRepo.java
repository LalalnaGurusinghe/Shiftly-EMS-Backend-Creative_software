package com.EMS.Employee.Management.System.repo;

import com.EMS.Employee.Management.System.entity.DesignationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DesignationRepo extends JpaRepository<DesignationEntity, Long> {
}
package com.EMS.Employee.Management.System.repo;

import com.EMS.Employee.Management.System.entity.CheckLeaveEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckLeaveRepo extends JpaRepository<CheckLeaveEntity, Integer> {
}
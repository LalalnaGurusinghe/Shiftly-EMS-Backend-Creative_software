package com.EMS.Employee.Management.System.repo;

import com.EMS.Employee.Management.System.entity.RequestLeaveEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestLeaveRepo extends JpaRepository<RequestLeaveEntity , Integer> {
}

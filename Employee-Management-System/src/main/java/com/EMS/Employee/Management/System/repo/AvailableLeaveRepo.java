package com.EMS.Employee.Management.System.repo;

import com.EMS.Employee.Management.System.entity.AvailableLeaveEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvailableLeaveRepo extends JpaRepository<AvailableLeaveEntity, Long> {
}

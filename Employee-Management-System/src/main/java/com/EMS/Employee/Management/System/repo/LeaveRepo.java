package com.EMS.Employee.Management.System.repo;

import com.EMS.Employee.Management.System.entity.LeaveEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRepo extends JpaRepository<LeaveEntity, Long> {
    List<LeaveEntity> findByUser_Id(Long userId);
    List<LeaveEntity> findByCoverPerson_EmployeeId(Integer coverPersonId);
    List<LeaveEntity> findByReportTo_EmployeeId(Integer reportToId);
} 
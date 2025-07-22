package com.EMS.Employee.Management.System.repo;

import com.EMS.Employee.Management.System.entity.ReferCandidateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReferCandidateRepo extends JpaRepository<ReferCandidateEntity, Long> {
    List<ReferCandidateEntity> findByUser_Id(Long userId);
}
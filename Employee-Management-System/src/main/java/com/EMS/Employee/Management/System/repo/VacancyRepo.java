package com.EMS.Employee.Management.System.repo;

import com.EMS.Employee.Management.System.entity.VacancyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VacancyRepo extends JpaRepository<VacancyEntity, Long> {
} 
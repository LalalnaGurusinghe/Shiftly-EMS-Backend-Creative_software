package com.EMS.Employee.Management.System.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.EMS.Employee.Management.System.entity.VacancyEntity;

@Repository
public interface VacancyRepo extends JpaRepository<VacancyEntity, Long> {
}

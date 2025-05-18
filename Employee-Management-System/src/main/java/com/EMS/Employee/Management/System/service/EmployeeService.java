package com.EMS.Employee.Management.System.service;


import com.EMS.Employee.Management.System.entity.Employee;
import com.EMS.Employee.Management.System.repo.EmployeeRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
public class EmployeeService {

    public final EmployeeRepo employeeRepo;

    public EmployeeService(EmployeeRepo employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    public Optional<Employee> getEmployeeById(Long id){
        return  employeeRepo.findById(id);
    }
}

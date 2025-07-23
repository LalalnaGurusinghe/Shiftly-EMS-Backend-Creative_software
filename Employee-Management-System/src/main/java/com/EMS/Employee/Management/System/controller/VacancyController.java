package com.EMS.Employee.Management.System.controller;

import com.EMS.Employee.Management.System.dto.VacancyDTO;
import com.EMS.Employee.Management.System.service.VacancyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/shiftly/ems/vacancies")
@CrossOrigin(origins = "http://localhost:3000")
public class VacancyController {
    @Autowired
    private VacancyService vacancyService;



    // Employee: View all vacancies
    @GetMapping("/all")
    public ResponseEntity<List<VacancyDTO>> getAllVacancies() {
        List<VacancyDTO> vacancies = vacancyService.getAllVacancies();
        return ResponseEntity.ok(vacancies);
    }

    // Employee: View single vacancy
    @GetMapping("/{id}")
    public ResponseEntity<VacancyDTO> getVacancyById(@PathVariable Long id) {
        VacancyDTO vacancy = vacancyService.getVacancyById(id);
        if (vacancy == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(vacancy);
    }
} 
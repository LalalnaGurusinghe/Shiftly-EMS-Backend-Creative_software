package com.EMS.Employee.Management.System.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.EMS.Employee.Management.System.dto.VacancyDTO;
import com.EMS.Employee.Management.System.service.VacancyService;

@RestController
@RequestMapping("/api/v1/shiftly/ems/vacancies")
@CrossOrigin(origins = "http://localhost:3000")
@PreAuthorize("hasRole('ADMIN')")
public class VacancyController {

    private final VacancyService vacancyService;
    
    public VacancyController(VacancyService vacancyService){
        this.vacancyService = vacancyService;
    }

    @PostMapping("/add/{departmentId}")
    public ResponseEntity<VacancyDTO> createVacancy(
            @PathVariable Long departmentId,
            @RequestBody VacancyDTO dto) {
        dto.setDepartmentId(departmentId);
        return ResponseEntity.ok(vacancyService.createVacancy(dto));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<VacancyDTO> updateVacancy(@PathVariable Long id, @RequestBody VacancyDTO dto) {
        return ResponseEntity.ok(vacancyService.updateVacancy(id, dto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteVacancy(@PathVariable Long id) {
        vacancyService.deleteVacancy(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<VacancyDTO>> getAllVacancies() {
        List<VacancyDTO> vacancies = vacancyService.getAllVacancies();
        return ResponseEntity.ok(vacancies);
    }

    @GetMapping("/by-department/{departmentId}")
    public ResponseEntity<List<VacancyDTO>> getVacanciesByDepartmentId(@PathVariable Long departmentId) {
        return ResponseEntity.ok(vacancyService.getVacancyByDepartmentId(departmentId));
    }


    // @GetMapping("/{id}")
    // public ResponseEntity<VacancyDTO> getVacancyById(@PathVariable Long id) {
    //     VacancyDTO vacancy = vacancyService.getVacancyById(id);
    //     if (vacancy == null) return ResponseEntity.notFound().build();
    //     return ResponseEntity.ok(vacancy);
    // }
} 
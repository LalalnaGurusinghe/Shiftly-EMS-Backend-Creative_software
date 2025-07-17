package com.EMS.Employee.Management.System.service.impl;

import com.EMS.Employee.Management.System.dto.VacancyDTO;
import com.EMS.Employee.Management.System.entity.VacancyEntity;
import com.EMS.Employee.Management.System.repo.VacancyRepo;
import com.EMS.Employee.Management.System.service.VacancyService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VacancyServiceImpl implements VacancyService {
    private final VacancyRepo vacancyRepo;

    public VacancyServiceImpl(VacancyRepo vacancyRepo) {
        this.vacancyRepo = vacancyRepo;
    }

    @Override
    public VacancyDTO addVacancy(VacancyDTO vacancyDTO) {
        VacancyEntity entity = new VacancyEntity();
        BeanUtils.copyProperties(vacancyDTO, entity);
        VacancyEntity saved = vacancyRepo.save(entity);
        VacancyDTO dto = new VacancyDTO();
        BeanUtils.copyProperties(saved, dto);
        return dto;
    }

    @Override
    public VacancyDTO updateVacancy(Long id, VacancyDTO vacancyDTO) {
        VacancyEntity entity = vacancyRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Vacancy not found"));
        if (vacancyDTO.getName() != null) entity.setName(vacancyDTO.getName());
        VacancyEntity saved = vacancyRepo.save(entity);
        VacancyDTO dto = new VacancyDTO();
        BeanUtils.copyProperties(saved, dto);
        return dto;
    }

    @Override
    public void deleteVacancy(Long id) {
        vacancyRepo.deleteById(id);
    }

    @Override
    public List<VacancyDTO> getAllVacancies() {
        return vacancyRepo.findAll().stream().map(entity -> {
            VacancyDTO dto = new VacancyDTO();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public VacancyDTO getVacancyById(Long id) {
        Optional<VacancyEntity> entity = vacancyRepo.findById(id);
        if (entity.isEmpty()) return null;
        VacancyDTO dto = new VacancyDTO();
        BeanUtils.copyProperties(entity.get(), dto);
        return dto;
    }
} 
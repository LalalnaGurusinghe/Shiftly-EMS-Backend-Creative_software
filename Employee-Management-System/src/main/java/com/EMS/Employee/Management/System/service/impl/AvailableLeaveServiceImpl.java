package com.EMS.Employee.Management.System.service.impl;

import com.EMS.Employee.Management.System.dto.AvailableLeaveDTO;
import com.EMS.Employee.Management.System.entity.AvailableLeaveEntity;
import com.EMS.Employee.Management.System.repo.AvailableLeaveRepo;
import com.EMS.Employee.Management.System.service.AvailableLeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AvailableLeaveServiceImpl implements AvailableLeaveService {

    @Autowired
    private AvailableLeaveRepo availableLeaveRepo;

    @Override
    public List<AvailableLeaveDTO> getAllAvailableLeaves() {
        List<AvailableLeaveEntity> entities = availableLeaveRepo.findAll();
        return entities.stream()
                .map(entity -> AvailableLeaveDTO.builder()
                        .type(entity.getType())
                        .count(entity.getCount())
                        .hexColor(entity.getHexColor())
                        .build())
                .collect(Collectors.toList());
    }
}

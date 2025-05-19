package com.EMS.Employee.Management.System.service;

import com.EMS.Employee.Management.System.dto.AvailableLeaveDTO;

import java.util.List;

public interface AvailableLeaveService {
    List<AvailableLeaveDTO> getAllAvailableLeaves();
}

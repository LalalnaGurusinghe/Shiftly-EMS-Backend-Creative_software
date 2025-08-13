package com.EMS.Employee.Management.System.service;

import com.EMS.Employee.Management.System.dto.LetterRequestDto;
import com.EMS.Employee.Management.System.entity.LetterRequest;
import java.util.List;

public interface LetterRequestService {
    LetterRequestDto createRequest(int employeeId, LetterRequestDto dto);
    List<LetterRequestDto> getByEmployeeId(int employeeId);
    List<LetterRequestDto> getAllRequests();
    List<LetterRequestDto> getRequestsForAdmin(Long adminUserId);
    String generateLetter(Long requestId);
    void deleteRequest(Long id);
    LetterRequestDto updateStatus(Long id, String status);
    LetterRequestDto updateRequest(Long id, LetterRequestDto dto);
}

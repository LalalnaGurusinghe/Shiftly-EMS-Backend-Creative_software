package com.EMS.Employee.Management.System.service;

import com.EMS.Employee.Management.System.dto.LetterRequestDto;
import com.EMS.Employee.Management.System.entity.LetterRequest;
import java.util.List;

public interface LetterRequestService {
    LetterRequestDto createRequest(LetterRequestDto dto, String username);
    List<LetterRequestDto> getUserRequests(String username);
    List<LetterRequestDto> getAllRequests();
    String generateLetter(Long requestId);
    void deleteRequest(Long id, String username);
}

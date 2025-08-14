package com.EMS.Employee.Management.System.service;

import com.EMS.Employee.Management.System.dto.ClaimDTO;
import com.EMS.Employee.Management.System.dto.EventDTO;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;
import java.util.List;

public interface EventService {
        EventDTO create(int employeeid,String title, String eventType, String enableDate, String expireDate,
                        MultipartFile image) throws Exception;

        List<EventDTO> getEventsForAdmin(Long adminUserId);
        List<EventDTO> getByEmployeeId(int employeeId);
        List<EventDTO> getAllEvents();
        void deleteEvent(Long id);
        EventDTO updateStatus(Long id,String status);
        EventDTO updateEvent(Long id, String title, String eventType, String enableDate, String expireDate,
                        MultipartFile image) throws Exception;

}
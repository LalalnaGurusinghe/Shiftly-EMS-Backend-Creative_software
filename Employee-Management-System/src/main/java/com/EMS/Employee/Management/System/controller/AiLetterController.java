package com.EMS.Employee.Management.System.controller;

import com.EMS.Employee.Management.System.service.AiLetterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/shiftly/ems/ai-letter")
@CrossOrigin(origins = "*")
public class AiLetterController {
    private final AiLetterService aiLetterService;

    public AiLetterController(AiLetterService aiLetterService) {
        this.aiLetterService = aiLetterService;
    }

    @PostMapping("/generate")
    public ResponseEntity<?> generateLetter(@RequestBody Map<String, Object> request) {
        String letterType = (String) request.get("letterType");
        Map<String, Object> fields = (Map<String, Object>) request.get("fields");
        if (letterType == null || fields == null) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Missing letterType or fields"));
        }
        String letterHtml = aiLetterService.generateLetter(letterType, fields);
        if (letterHtml == null) {
            return ResponseEntity.ok(Map.of("success", false, "message", "We are currently experiencing technical difficulties. Please try again later."));
        }
        return ResponseEntity.ok(Map.of("success", true, "letterHtml", letterHtml));
    }
} 
package com.EMS.Employee.Management.System.service;

import java.util.Map;

public interface AiLetterService {
    /**
     * Generates a letter using Gemini AI based on the letter type and provided fields.
     * @param letterType The type of letter to generate.
     * @param fields The fields required for the letter.
     * @return The generated letter as HTML, or null if generation failed.
     */
    String generateLetter(String letterType, Map<String, Object> fields);
} 
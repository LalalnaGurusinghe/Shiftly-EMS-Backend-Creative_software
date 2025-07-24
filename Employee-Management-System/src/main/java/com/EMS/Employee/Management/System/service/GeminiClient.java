package com.EMS.Employee.Management.System.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class GeminiClient {

    private final WebClient webClient;
    private final String apiKey;
    private static final String GEMINI_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";

    public GeminiClient(@Value("${gemini.api.key}") String apiKey) {
        this.apiKey = apiKey;
        this.webClient = WebClient.builder()
                .baseUrl(GEMINI_URL + "?key=" + apiKey)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public String generateLetter(String prompt) {
        String requestBody = "{"
            + "\"contents\":[{"
            + "\"role\":\"user\","
            + "\"parts\":[{\"text\":\"" + prompt.replace("\"", "\\\"") + "\"}]"
            + "}]"
            + "}";
        System.out.println("[GeminiClient] Prompt: " + prompt);
        System.out.println("[GeminiClient] Request Body: " + requestBody);
        try {
            String response = webClient.post()
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            System.out.println("[GeminiClient] Response: " + response);
            return extractTextFromGeminiResponse(response);
        } catch (Exception e) {
            System.out.println("[GeminiClient] Exception: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Extract the generated text from Gemini's JSON response
    private String extractTextFromGeminiResponse(String response) {
        // Gemini's response format: { "candidates": [ { "content": { "parts": [ { "text": "..." } ] } } ] }
        // Simple extraction (for production, use a JSON parser like Jackson)
        if (response == null) return null;
        int idx = response.indexOf("\"text\":");
        if (idx == -1) return null;
        int start = response.indexOf('"', idx + 7) + 1;
        int end = response.indexOf('"', start);
        if (start == 0 || end == -1) return null;
        return response.substring(start, end).replace("\\n", "<br>");
    }
} 

package com.example.demo.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AiSimplificationService {

    @Value("${groq.api-key:}")
    private String apiKey;

    @Value("${groq.api-url:https://api.groq.com/openai/v1/chat/completions}")
    private String apiUrl;

    @Value("${groq.model:openai/gpt-oss-120b}")
    private String model;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();

    public String simplifyInstructions(String originalInstructions) {
        if (originalInstructions == null
                || originalInstructions.isBlank()) {
            throw new IllegalArgumentException(
                    "Discharge instructions cannot be empty.");
        }

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException(
                    "Groq API key is not configured. Please set the GROQ_API_KEY environment variable.");
        }

        String prompt = """
                Rewrite the doctor's discharge instructions in
                simple, clear English for a patient.

                Rules:
                - Preserve all medical facts and instructions.
                - Do not add new medical advice.
                - Do not change medicine names, dosages, frequency,
                  timing, or follow-up dates.
                - Use short sentences and simple words.
                - Return only the simplified instructions.

                Original instructions:
                """ + originalInstructions;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(apiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> requestBody = Map.of(
                    "model", model,
                    "messages", List.of(
                            Map.of(
                                    "role", "user",
                                    "content", prompt
                            )
                    ),
                    "temperature", 0
            );


            HttpEntity<Map<String, Object>> request =
                    new HttpEntity<>(requestBody, headers);

            String response = restTemplate.postForObject(
                    apiUrl, request, String.class);

            JsonNode root = objectMapper.readTree(response);
            String simplified = root.path("choices")
                    .path(0)
                    .path("message")
                    .path("content")
                    .asText("");

            if (simplified.isBlank()) {
                throw new IllegalStateException(
                        "Groq returned an empty response.");
            }

            return simplified.trim();

        } catch (Exception e) {
            throw new IllegalStateException(
                    "Unable to simplify instructions using Groq: "
                            + e.getMessage(), e);
        }
    }
}
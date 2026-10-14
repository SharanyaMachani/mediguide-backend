
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
public class TeluguTranslationService {

    @Value("${groq.api-key:}")
    private String apiKey;

    @Value("${groq.api-url:https://api.groq.com/openai/v1/chat/completions}")
    private String apiUrl;

    @Value("${groq.model:openai/gpt-oss-120b}")
    private String model;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();

    public String translateToTelugu(String englishInstructions) {

        if (englishInstructions == null
                || englishInstructions.isBlank()) {
            throw new IllegalArgumentException(
                    "English instructions cannot be empty.");
        }

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException(
                    "Groq API key is not configured. Please set the GROQ_API_KEY environment variable.");
        }

        String prompt = """
                Translate the following patient discharge instructions
                into clear, natural Telugu.

                Rules:
                - Translate all instructions completely.
                - Preserve medicine names, dosages, frequency,
                  timing, numbers, and follow-up dates exactly.
                - Do not add medical advice or omit information.
                - Return only the Telugu translation.

                Instructions:
                """ + englishInstructions;

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
            String translated = root.path("choices")
                    .path(0)
                    .path("message")
                    .path("content")
                    .asText("");

            if (translated.isBlank()) {
                throw new IllegalStateException(
                        "Groq returned an empty translation.");
            }

            return translated.trim();

        } catch (Exception e) {
            throw new IllegalStateException(
                    "Unable to translate instructions using Groq: "
                            + e.getMessage(), e);
        }
    }
}
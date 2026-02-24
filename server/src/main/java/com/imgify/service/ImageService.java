package com.imgify.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;

@Service
public class ImageService {

    @Value("${clipdrop.api.key}")
    private String apiKey;

    @Value("${clipdrop.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public String generateImage(String prompt) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-api-key", apiKey);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("prompt", prompt);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<byte[]> response = restTemplate.exchange(
                    apiUrl,
                    HttpMethod.POST,
                    requestEntity,
                    byte[].class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return "data:image/png;base64," + Base64.getEncoder().encodeToString(response.getBody());
            } else {
                throw new RuntimeException("Failed to generate image: " + response.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error calling ClipDrop API: " + e.getMessage());
        }
    }
}

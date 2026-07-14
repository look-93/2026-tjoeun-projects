package com.moit.review.client;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ModerationClient {


    @Value("${openai.api.key}")
    private String apiKey;


    public boolean checkContent(String content) {


        HttpHeaders headers = new HttpHeaders();

        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);


        Map<String, Object> body = new HashMap<>();
      
        body.put("model", "omni-moderation-latest");
        body.put("input", content);


        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);


        try {

            RestTemplate restTemplate = new RestTemplate();


            ResponseEntity<String> response =
                    restTemplate.postForEntity(
                            "https://api.openai.com/v1/moderations",
                            request,
                            String.class
                    );


            ObjectMapper mapper = new ObjectMapper();

            JsonNode root =
                    mapper.readTree(response.getBody());


           
            boolean flagged =
                    root.path("results")
                        .get(0)
                        .path("flagged")
                        .asBoolean();


            return flagged;


        } catch(Exception e) {

            System.err.println("=== 욕설 필터 API 오류 ===");
            e.printStackTrace();

            // API 오류로 저장 막지 않도록 false 처리
            return false;
        }
    }
}
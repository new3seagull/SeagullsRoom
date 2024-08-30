package com.new3seagull.SeagullsRoom.domain.gpt.controller;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.new3seagull.SeagullsRoom.domain.gpt.dto.ChatGPTRequest;
import com.new3seagull.SeagullsRoom.domain.gpt.dto.ChatGPTResponse;
import com.new3seagull.SeagullsRoom.global.util.ImageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/gpt")
@RequiredArgsConstructor
public class ChatGPTController {
    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;

    private final RestTemplate restTemplate;


    @PostMapping("/chat")
    public String requestGpt(@RequestPart("image") MultipartFile imageFile) {
        String category = null;
        try {
            String base64Image = ImageUtils.encodeImageToBase64(imageFile);
            ChatGPTRequest request = new ChatGPTRequest(model, base64Image);

            // API 호출하여 응답 받기
            ChatGPTResponse response = restTemplate.postForObject(apiURL, request, ChatGPTResponse.class);
            Object o = response.getChoices().get(0).getMessage().getFunction_call().getArguments().get("category");
            category = o.toString();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return category;
    }
}
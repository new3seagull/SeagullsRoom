package com.new3seagull.SeagullsRoom.domain.gpt.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.new3seagull.SeagullsRoom.domain.gpt.dto.ChatGPTRequest;
import com.new3seagull.SeagullsRoom.domain.gpt.dto.ChatGPTResponse;
import com.new3seagull.SeagullsRoom.global.util.ImageUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/gpt")
@RequiredArgsConstructor
@Tag(name = "ChatGPT", description = "ChatGPT API")
public class ChatGPTController {
    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @PostMapping(value = "/chat", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Request GPT for image analysis",
        description = "Sends an image to GPT for analysis and returns the category")
    @ApiResponse(responseCode = "200", description = "Successful operation",
        content = @Content(schema = @Schema(type = "string")))
    public String requestGpt(
        @Parameter(description = "Image file to be analyzed", required = true)
        @RequestPart("image") MultipartFile imageFile,
        @Parameter(description = "User-defined categories", required = true)
        @RequestPart("userCategories") String userCategoriesJson) {
        String category = null;
        try {
            String base64Image = ImageUtils.encodeImageToBase64(imageFile);

            // userCategories JSON 문자열을 List<String>으로 변환
            List<String> userCategories = objectMapper.readValue(userCategoriesJson, new TypeReference<List<String>>() {});

            // 사용자 정의 카테고리를 포함하여 ChatGPTRequest 생성
            ChatGPTRequest request = new ChatGPTRequest(model, base64Image, userCategories);

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
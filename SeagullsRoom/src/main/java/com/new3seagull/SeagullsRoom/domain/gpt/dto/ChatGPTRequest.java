package com.new3seagull.SeagullsRoom.domain.gpt.dto;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChatGPTRequest {
    private String model;
    private List<GptRequestMessage> messages;
    private List<GptFunctionDTO> functions;
    private FunctionCall function_call;
    private double temperature;
    private int max_tokens;
    private double top_p;
    private double frequency_penalty;
    private double presence_penalty;


    public ChatGPTRequest(String model, String base64image, List<String> userCategories) {
        this.model = model;
        this.messages = new ArrayList<>();
        List<Object> list = List.of(
                new ImgContent("image_url", new ImgContent.Img("data:image/jpeg;base64," + base64image)),
                new TextContent("text", "이미지를 설명해줘")
        );
        this.messages.add(new GptRequestMessage("user", list));

        this.functions = new ArrayList<>();


        List<String> allCategories = Stream.concat(
            Stream.of("GAME", "SNS", "OTHER"),
            userCategories.stream()
        ).distinct().collect(Collectors.toList());

        this.functions.add(GptFunctionDTO.builder()
            .name("category")
            .description("주어진 이미지가 어떤 카테고리에 해당하는지 반환")
            .parameters(GptFunctionDTO.Parameters.builder()
                .type("object")
                .properties(GptFunctionDTO.Properties.builder()
                    .category(GptFunctionDTO.Category.builder()
                        .type("string")
                        .description("이미지를 카테고리 중 분류하기")
                        .enums(allCategories)
                        .build())
                    .build())
                .required(List.of("category"))
                .build())
            .build());

        this.function_call = new FunctionCall("category");
        this.temperature = 0.0;
        this.max_tokens = 30;
        this.top_p = 1.0;
        this.frequency_penalty = -1.0;
        this.presence_penalty = -1.0;
    }
}
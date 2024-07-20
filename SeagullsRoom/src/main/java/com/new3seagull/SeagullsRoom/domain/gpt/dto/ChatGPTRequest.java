package com.new3seagull.SeagullsRoom.domain.gpt.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChatGPTRequest {
    private String model;
    private List<GptRequestMessage> messages;

    public ChatGPTRequest(String model, String base64image) {
        this.model = model;
        this.messages = new ArrayList<>();
        List<Object> list = List.of(new ImgContent("image_url",new ImgContent.Img("data:image/jpeg;base64," + base64image)), new TextContent("text", "다음 사진이 공부와 관련이 있으면 1을 없으면 0을 출력해 줘"));
        this.messages.add(new GptRequestMessage("user", list));
    }
}
package com.new3seagull.SeagullsRoom.domain.gpt.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Getter
public class ChatGPTResponse {
    private String id;
    private String object;
    private long created;
    private String model;
    private List<Choice> choices;
    private Usage usage;
    private String systemFingerprint;

    // Getters and Setters
    @Getter
    public static class Choice {
        private int index;
        private Message message;
        private Object logprobs;
        private String finishReason;

        // Getters and Setters
        @Getter
        public static class Message {
            private String role;
            private String content;
            private FunctionCall function_call;
            private Object refusal;

            // Getters and Setters
            @Getter
            public static class FunctionCall {
                private String name;
                private Map<String, Object> arguments;

                @JsonCreator
                public FunctionCall(@JsonProperty("name") String name,
                                    @JsonProperty("arguments") String argumentsJson) throws IOException {
                    this.name = name;
                    ObjectMapper objectMapper = new ObjectMapper();
                    this.arguments = objectMapper.readValue(argumentsJson, new TypeReference<Map<String, Object>>() {});
                }
                // Getters and Setters
            }
        }
    }
    @Getter
    public static class Usage {
        private int promptTokens;
        private int completionTokens;
        private int totalTokens;

        // Getters and Setters
    }
}
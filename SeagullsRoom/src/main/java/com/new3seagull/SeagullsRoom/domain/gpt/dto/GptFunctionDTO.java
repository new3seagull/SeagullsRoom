package com.new3seagull.SeagullsRoom.domain.gpt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class GptFunctionDTO {

    private String name;
    private String description;
    private Parameters parameters;

    @Getter
    @Builder
    public static class Parameters {

        private String type;
        private Properties properties;
        private List<String> required = new ArrayList<>();

    }
    @Getter
    @Builder
    public static class Properties {

        private Category category;

        // Getters and Setters

    }

    @Getter
    @Builder
    public static class Category {

        private String type;
        private String description;
        @JsonProperty("enum")
        private List<String> enums;


    }
}
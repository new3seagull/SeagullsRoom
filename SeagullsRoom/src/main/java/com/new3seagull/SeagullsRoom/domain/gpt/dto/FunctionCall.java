package com.new3seagull.SeagullsRoom.domain.gpt.dto;

import lombok.Getter;

@Getter
public class FunctionCall {
    private String name;

    public FunctionCall(String name) {
        this.name = name;
    }
}

package com.new3seagull.SeagullsRoom.domain.gpt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TextContent {
    private String type;
    private String text;
}
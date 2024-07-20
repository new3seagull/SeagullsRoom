package com.new3seagull.SeagullsRoom.domain.study.dto;

import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StduyAddRequestDto {
    private LocalTime studyTime;
}

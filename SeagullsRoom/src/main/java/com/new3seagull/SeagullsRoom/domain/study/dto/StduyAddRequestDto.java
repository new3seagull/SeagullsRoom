package com.new3seagull.SeagullsRoom.domain.study.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StduyAddRequestDto {

    @NotNull(message = "공부 시간은 필수 입력 항목입니다.")
    private LocalTime studyTime;
}

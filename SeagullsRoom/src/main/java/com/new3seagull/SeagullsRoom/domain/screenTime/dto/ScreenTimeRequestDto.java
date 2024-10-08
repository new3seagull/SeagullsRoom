package com.new3seagull.SeagullsRoom.domain.screenTime.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ScreenTimeRequestDto {

    @NotBlank(message = "카테고리는 비어있을 수 없습니다.")
    @Size(min = 1, max = 50, message = "카테고리는 1자 이상 50자 이하여야 합니다.")
    private String category;

    @NotNull(message = "카운트는 null일 수 없습니다.")
    @Positive(message = "카운트는 양수여야 합니다.")
    private Integer count;
}

package com.new3seagull.SeagullsRoom.domain.screenTime.dto;

import com.new3seagull.SeagullsRoom.domain.screenTime.entity.ScreenTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScreenTimeResponseDto {

    private String category;
    private Integer count;

    public static ScreenTimeResponseDto toDto(ScreenTime screenTime) {
        return ScreenTimeResponseDto.builder()
            .category(screenTime.getCategory())
            .count(screenTime.getCount())
            .build();
    }
}

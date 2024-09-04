package com.new3seagull.SeagullsRoom.domain.screenTime.dto;

import com.new3seagull.SeagullsRoom.domain.screenTime.entity.ScreenTime;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScreenTimeResponseDto {

    private Long id;
    private String category;
    private Integer count;
    private LocalDateTime updatedAt;

    public static ScreenTimeResponseDto toDto(ScreenTime screenTime) {
        return ScreenTimeResponseDto.builder()
            .id(screenTime.getId())
            .category(screenTime.getCategory())
            .count(screenTime.getCount())
            .updatedAt(screenTime.getUpdatedAt())
            .build();
    }
}

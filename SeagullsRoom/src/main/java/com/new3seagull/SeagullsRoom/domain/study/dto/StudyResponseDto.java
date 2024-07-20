package com.new3seagull.SeagullsRoom.domain.study.dto;

import com.new3seagull.SeagullsRoom.domain.study.entity.Study;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import com.new3seagull.SeagullsRoom.domain.study.entity.Study;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StudyResponseDto {

    private Long id;
    private String userEmail;
    private LocalTime studyTime;
    private LocalDateTime updatedAt;


    public static StudyResponseDto toDto(Study study) {
        return StudyResponseDto.builder()
            .id(study.getId())
            .userEmail(study.getUser().getEmail())
            .studyTime(study.getStudyTime())
            .updatedAt(study.getUpdatedAt())
            .build();
    }
}

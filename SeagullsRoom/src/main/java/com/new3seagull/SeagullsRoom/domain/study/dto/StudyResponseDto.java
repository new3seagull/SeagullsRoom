package com.new3seagull.SeagullsRoom.domain.study.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.new3seagull.SeagullsRoom.domain.study.entity.Study;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
public class StudyResponseDto {
    private final Long id;
    private final String userEmail;
    private final LocalTime studyTime;
    private final LocalDate studyDate;
    private final LocalDateTime updatedAt;

    @Builder
    private StudyResponseDto(Long id, String userEmail, LocalTime studyTime, LocalDate studyDate, LocalDateTime updatedAt) {
        this.id = id;
        this.userEmail = userEmail;
        this.studyTime = studyTime;
        this.studyDate = studyDate;
        this.updatedAt = updatedAt;
    }

    public static StudyResponseDto toDto(Study study) {
        return StudyResponseDto.builder()
                .id(study.getId())
                .userEmail(study.getUser().getEmail())
                .studyTime(study.getStudyTime())
                .studyDate(study.getStudyDate())
                .updatedAt(study.getUpdatedAt())
                .build();
    }


}

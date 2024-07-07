package com.new3seagull.SeagullsRoom.domain.study.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StudyResponseDto {
    private Long id;
    private String userEmail;
    private LocalTime studyTime;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

package com.new3seagull.SeagullsRoom.domain.study.dto;

import java.time.LocalTime;
import lombok.Getter;

@Getter
public class StudyResponseDto {
    private Long id;
    private String userEmail;
    private LocalTime studyTime;

    public StudyResponseDto(Long id, String userEmail, LocalTime studyTime) {
        this.id = id;
        this.userEmail = userEmail;
        this.studyTime = studyTime;
    }
}

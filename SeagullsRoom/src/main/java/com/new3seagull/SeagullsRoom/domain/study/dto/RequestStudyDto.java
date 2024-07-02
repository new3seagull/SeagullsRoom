package com.new3seagull.SeagullsRoom.domain.study.dto;

import com.new3seagull.SeagullsRoom.domain.user.entity.User;
import java.time.LocalTime;

public class RequestStudyDto {
    Long id;
    User userId;
    LocalTime studyTime;
}

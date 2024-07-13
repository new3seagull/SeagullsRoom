package com.new3seagull.SeagullsRoom.domain.study.controller;

import com.new3seagull.SeagullsRoom.domain.study.dto.StudyResponseDto;
import com.new3seagull.SeagullsRoom.domain.study.entity.Study;
import com.new3seagull.SeagullsRoom.domain.study.service.StudyService;
import com.new3seagull.SeagullsRoom.domain.user.entity.User;
import com.new3seagull.SeagullsRoom.domain.user.service.UserService;
import java.security.Principal;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.new3seagull.SeagullsRoom.global.util.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.format.annotation.DateTimeFormat;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/study")
public class StudyController {

    private final StudyService studyService;
    private final UserService userService;

    // 유저의 공부 시간 조회
    @GetMapping
    public ResponseEntity<List<StudyResponseDto>> getStudytimesByUserId(Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        List<Study> studies = studyService.getStudytimesByUser(user);
        List<StudyResponseDto> responseDtos = studies.stream()
            .map(StudyResponseDto::toDto)
            .collect(Collectors.toList());
        return ResponseEntity.ok(responseDtos);
    }

    // 스터디 아이디를 통해 시간 조회
    @GetMapping("/{id}")
    public ResponseEntity<Study> getStudyById(Principal principal, @PathVariable Long id) {
        User user = userService.getUserByEmail(principal.getName());
        Study study = studyService.getStudyById(user, id);
        return ResponseEntity.ok(study);
    }

    @PostMapping
    public ResponseEntity<?> recordStudyTime(Principal principal,
                                                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime studyTime) {
        User user = userService.getUserByEmail(principal.getName());
        Study recordedStudy = studyService.recordStudyTime(user, studyTime);


        return ResponseEntity.status(HttpStatus.CREATED).body(ApiUtils.success(StudyResponseDto.toDto(recordedStudy)));
    }

    @GetMapping("/top10")
    public ResponseEntity<?> rankStudyTime() {
        return ResponseEntity.ok(ApiUtils.success(studyService.getTop10StudyTimes()));
    }


}

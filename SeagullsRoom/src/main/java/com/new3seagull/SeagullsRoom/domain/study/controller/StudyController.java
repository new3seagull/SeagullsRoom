package com.new3seagull.SeagullsRoom.domain.study.controller;

import com.new3seagull.SeagullsRoom.domain.study.service.StudyService;
import com.new3seagull.SeagullsRoom.domain.user.entity.User;
import java.time.LocalTime;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/study")
public class StudyController {

    private final StudyService studyService;

    @GetMapping()
    public ResponseEntity<LocalTime> getStudytime(@RequestParam User userId) {
        LocalTime studyTime = studyService.getStudytimeByUserId(userId);
        return ResponseEntity.ok(studyTime);
    }

//    @PostMapping()
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity<StudyResponseDto> recordStudyTime(@RequestBody RequestStudyDto requestStudyDto) {
//        StudyResponseDto response = studyService.recordStudyTime(requestStudyDto);
//        return ResponseEntity.ok(response);
//    }


}

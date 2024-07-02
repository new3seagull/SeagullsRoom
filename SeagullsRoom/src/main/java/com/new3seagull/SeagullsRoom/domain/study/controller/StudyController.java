package com.new3seagull.SeagullsRoom.domain.study.controller;

import com.new3seagull.SeagullsRoom.domain.study.entity.Study;
import com.new3seagull.SeagullsRoom.domain.study.service.StudyService;
import com.new3seagull.SeagullsRoom.domain.user.entity.User;
import java.util.List;
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

    //todo - 토큰 받아서 개인 공부 시간만 조회 가능하도록 수정
    @GetMapping()
    public ResponseEntity<List<Study>> getStudytimesByUserId(@RequestParam User userId) {
        List<Study> studyTime = studyService.getStudytimesByUserId(userId);
        return ResponseEntity.ok(studyTime);
    }

//    @PostMapping()
//    @ResponseStatus(HttpStatus.OK)
//    public ResponseEntity<StudyResponseDto> recordStudyTime(@RequestBody RequestStudyDto requestStudyDto) {
//        StudyResponseDto response = studyService.recordStudyTime(requestStudyDto);
//        return ResponseEntity.ok(response);
//    }


}

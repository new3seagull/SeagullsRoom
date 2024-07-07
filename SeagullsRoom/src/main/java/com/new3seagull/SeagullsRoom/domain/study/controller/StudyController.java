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

    @GetMapping
    public ResponseEntity<List<Study>> getStudytimesByUserId(Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        List<Study> studyTime = studyService.getStudytimesByUser(user);
        return ResponseEntity.ok(studyTime);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Study> getStudyById(Principal principal, @PathVariable Long id) {
        User user = userService.getUserByEmail(principal.getName());
        Study study = studyService.getStudyById(user, id);
        return ResponseEntity.ok(study);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> recordStudyTime(Principal principal,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime studyTime) {
        User user = userService.getUserByEmail(principal.getName());
        Study recordedStudy = studyService.recordStudyTime(user, studyTime);
        StudyResponseDto recordedStudyTime = recordedStudy.toDto();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "공부 기록이 성공적으로 추가되었습니다.");
        response.put("data", recordedStudyTime);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


}

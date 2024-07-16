package com.new3seagull.SeagullsRoom.domain.study.controller;

import com.new3seagull.SeagullsRoom.domain.study.dto.StduyAddRequestDto;
import com.new3seagull.SeagullsRoom.domain.study.dto.StudyResponseDto;
import com.new3seagull.SeagullsRoom.domain.study.entity.Study;
import com.new3seagull.SeagullsRoom.domain.study.service.StudyService;
import com.new3seagull.SeagullsRoom.domain.user.entity.User;
import com.new3seagull.SeagullsRoom.domain.user.service.UserService;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.new3seagull.SeagullsRoom.global.util.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.format.annotation.DateTimeFormat;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/studies")
public class StudyController {

    private final StudyService studyService;
    private final UserService userService;

    // 유저의 공부 시간 조회
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<StudyResponseDto> getStudytimesByUserId(Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        return studyService.getStudytimesByUser(user);
    }

    // 스터디 아이디를 통해 시간 조회
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StudyResponseDto getStudyById(Principal principal, @PathVariable Long id) {
        User user = userService.getUserByEmail(principal.getName());
        return studyService.getStudyById(user, id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StudyResponseDto recordStudyTime(Principal principal,
        @RequestBody @DateTimeFormat(iso = ISO.TIME) StduyAddRequestDto request) {
        User user = userService.getUserByEmail(principal.getName());
        return studyService.recordStudyTime(user, request.getStudyTime());
    }

    @GetMapping("/top10")
    public ResponseEntity<?> rankStudyTime() {
        return ResponseEntity.ok(ApiUtils.success(studyService.getTop10StudyTimes(LocalDate.now())));
    }

    @GetMapping("/user/date")
    public ResponseEntity<?> dateStudyTime(Principal principal) {
        return ResponseEntity.ok(ApiUtils.success(studyService.getStudyTimeByDate(principal, LocalDate.now())));
    }
    @GetMapping("/user/month")
    public ResponseEntity<?> monthStudyTime(Principal principal) {
        return ResponseEntity.ok(ApiUtils.success(studyService.getStudyTimeByMonth(principal, LocalDate.now())));
    }
}

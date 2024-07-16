package com.new3seagull.SeagullsRoom.domain.study.controller;

import com.new3seagull.SeagullsRoom.domain.study.dto.StduyAddRequestDto;
import com.new3seagull.SeagullsRoom.domain.study.dto.StudyResponseDto;
import com.new3seagull.SeagullsRoom.domain.study.entity.Study;
import com.new3seagull.SeagullsRoom.domain.study.service.StudyService;
import com.new3seagull.SeagullsRoom.domain.user.entity.User;
import com.new3seagull.SeagullsRoom.domain.user.service.UserService;
import java.security.Principal;
import java.time.LocalTime;
import java.util.List;
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
@RequestMapping("/api/v1/study")
public class StudyController {

    private final StudyService studyService;
    private final UserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<StudyResponseDto> getStudytimesByUserId(Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        return studyService.getStudytimesByUser(user);
    }

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
}

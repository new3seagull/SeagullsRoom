package com.new3seagull.SeagullsRoom.domain.screenTime.controller;

import com.new3seagull.SeagullsRoom.domain.screenTime.dto.ScreenTimeRequestDto;
import com.new3seagull.SeagullsRoom.domain.screenTime.dto.ScreenTimeResponseDto;
import com.new3seagull.SeagullsRoom.domain.screenTime.service.ScreenTimeService;
import jakarta.validation.Valid;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/screenTime")
public class ScreenTimeController {

    private final ScreenTimeService screenTimeService;

    @PostMapping
    public ResponseEntity<ScreenTimeResponseDto> addScreenTime(Principal principal,
        @Valid @RequestBody ScreenTimeRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(screenTimeService.addScreenTime(principal.getName(), requestDto));
    }
}

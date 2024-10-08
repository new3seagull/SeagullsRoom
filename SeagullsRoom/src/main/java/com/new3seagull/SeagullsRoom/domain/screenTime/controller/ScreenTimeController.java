package com.new3seagull.SeagullsRoom.domain.screenTime.controller;

import com.new3seagull.SeagullsRoom.domain.screenTime.dto.ScreenTimeRequestDto;
import com.new3seagull.SeagullsRoom.domain.screenTime.dto.ScreenTimeResponseDto;
import com.new3seagull.SeagullsRoom.domain.screenTime.service.ScreenTimeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/screenTime")
@Tag(name = "Screen Time", description = "Screen Time management API")
public class ScreenTimeController {

    private final ScreenTimeService screenTimeService;

    @GetMapping
    @Operation(summary = "Get screen time", description = "Retrieve screen time for the authenticated user")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved screen time",
        content = @Content(schema = @Schema(implementation = ScreenTimeResponseDto.class)))
    public ResponseEntity<List<ScreenTimeResponseDto>> getScreenTime(
        @Parameter(description = "Authenticated user", hidden = true) Principal principal) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(screenTimeService.getScreenTime(principal.getName()));
    }

    @PostMapping
    @Operation(summary = "Add screen time", description = "Add new screen time entry for the authenticated user")
    @ApiResponse(responseCode = "201", description = "Successfully added screen time",
        content = @Content(schema = @Schema(implementation = ScreenTimeResponseDto.class)))
    public ResponseEntity<ScreenTimeResponseDto> addScreenTime(
        @Parameter(description = "Authenticated user", hidden = true) Principal principal,
        @Parameter(description = "Screen time details", required = true)
        @Valid @RequestBody ScreenTimeRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(screenTimeService.addScreenTime(principal.getName(), requestDto));
    }
}
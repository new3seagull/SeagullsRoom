package com.new3seagull.SeagullsRoom.domain.study.controller;

import com.new3seagull.SeagullsRoom.domain.study.dto.StduyAddRequestDto;
import com.new3seagull.SeagullsRoom.domain.study.dto.StudyResponseDto;
import com.new3seagull.SeagullsRoom.domain.study.service.StudyService;
import com.new3seagull.SeagullsRoom.domain.user.entity.User;
import com.new3seagull.SeagullsRoom.domain.user.service.UserService;
import com.new3seagull.SeagullsRoom.global.util.ApiUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/studies")
@Tag(name = "Study", description = "Study time management API")
public class StudyController {

    private final StudyService studyService;
    private final UserService userService;

    @GetMapping
    @Operation(summary = "Get user's study times", description = "Retrieves all study times for the authenticated user")
    @ApiResponse(responseCode = "200", description = "Successful operation",
        content = @Content(schema = @Schema(implementation = StudyResponseDto.class)))
    public ResponseEntity<List<StudyResponseDto>> getStudytimesByUserId(
        @Parameter(hidden = true) Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        return ResponseEntity.ok(studyService.getStudytimesByUser(user));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get study time by ID", description = "Retrieves a specific study time entry by its ID")
    @ApiResponse(responseCode = "200", description = "Successful operation",
        content = @Content(schema = @Schema(implementation = StudyResponseDto.class)))
    public ResponseEntity<StudyResponseDto> getStudyById(
        @Parameter(hidden = true) Principal principal,
        @Parameter(description = "Study time entry ID", required = true) @PathVariable Long id) {
        User user = userService.getUserByEmail(principal.getName());
        return ResponseEntity.ok(studyService.getStudyById(user, id));
    }

    @PostMapping
    @Operation(summary = "Record study time", description = "Records a new study time entry for the authenticated user")
    @ApiResponse(responseCode = "201", description = "Study time recorded successfully",
        content = @Content(schema = @Schema(implementation = StudyResponseDto.class)))
    public ResponseEntity<StudyResponseDto> recordStudyTime(
        @Parameter(hidden = true) Principal principal,
        @Parameter(description = "Study time details", required = true) @Valid @RequestBody @DateTimeFormat(iso = ISO.TIME) StduyAddRequestDto request) {
        User user = userService.getUserByEmail(principal.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(studyService.recordStudyTime(user, request.getStudyTime()));
    }

    @GetMapping("/top10")
    @Operation(summary = "Get top 10 study times", description = "Retrieves the top 10 study times for the current date")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    public ResponseEntity<?> rankStudyTime() {
        return ResponseEntity.ok(ApiUtils.success(studyService.getTop10StudyTimes(LocalDate.now())));
    }

    @GetMapping("/user/date")
    @Operation(summary = "Get user's study time by date", description = "Retrieves the study time for the authenticated user for the current date")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    public ResponseEntity<?> dateStudyTime(@Parameter(hidden = true) Principal principal) {
        return ResponseEntity.ok(ApiUtils.success(studyService.getStudyTimeByDate(principal, LocalDate.now())));
    }

    @GetMapping("/user/month")
    @Operation(summary = "Get user's study time by month", description = "Retrieves the study time for the authenticated user for the current month")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    public ResponseEntity<?> monthStudyTime(@Parameter(hidden = true) Principal principal) {
        return ResponseEntity.ok(ApiUtils.success(studyService.getStudyTimeByMonth(principal, LocalDate.now())));
    }
}
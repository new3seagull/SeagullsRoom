package com.new3seagull.SeagullsRoom.domain.user.controller;

import com.new3seagull.SeagullsRoom.domain.user.dto.JoinDto;
import com.new3seagull.SeagullsRoom.domain.user.service.JoinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
@Tag(name = "User", description = "User management API")
public class JoinController {

    private final JoinService joinService;

    @PostMapping(value = "/api/v1/users", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Join new user", description = "Register a new user to the system")
    @ApiResponse(responseCode = "302", description = "User registered successfully, redirecting to login page")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    public String joinProcess(
        @Parameter(description = "User registration details", required = true)
        @RequestBody JoinDto joinDto) {
        joinService.joinProcess(joinDto);
        return "redirect:/login.html";
    }
}
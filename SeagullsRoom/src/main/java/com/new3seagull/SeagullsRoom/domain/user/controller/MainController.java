package com.new3seagull.SeagullsRoom.domain.user.controller;

import com.new3seagull.SeagullsRoom.global.util.ApiUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@Tag(name = "Main", description = "Main page API")
public class MainController {

    @GetMapping("/")
    @Operation(summary = "Get main page", description = "Retrieves the main page content for the authenticated user")
    @ApiResponse(responseCode = "200", description = "Successful operation",
        content = @Content(schema = @Schema(implementation = String.class)))
    public ResponseEntity<?> mainP(
        @Parameter(hidden = true) Principal principal) {
        System.out.println(ApiUtils.success(principal.getName()));
        return ResponseEntity.ok(ApiUtils.success(principal.getName()));
    }
}
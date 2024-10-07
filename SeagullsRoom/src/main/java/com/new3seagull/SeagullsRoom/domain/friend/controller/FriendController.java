package com.new3seagull.SeagullsRoom.domain.friend.controller;

import com.new3seagull.SeagullsRoom.domain.friend.dto.FriendCountDto;
import com.new3seagull.SeagullsRoom.domain.friend.dto.FriendRequestDto;
import com.new3seagull.SeagullsRoom.domain.friend.dto.FriendResponseDto;
import com.new3seagull.SeagullsRoom.domain.friend.service.FriendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/friend")
@Tag(name = "Friend Management", description = "Friend management API")
public class FriendController {

    private final FriendService friendService;

    @GetMapping
    @Operation(summary = "Get all friends of the current user")
    public ResponseEntity<List<FriendResponseDto>> getFriends(
        @Parameter(hidden = true) Principal principal) {
        return ResponseEntity.ok(friendService.getFriendsByUserEmail(principal.getName()));
    }

    @GetMapping("/count")
    @Operation(summary = "Get friend count of the current user")
    public ResponseEntity<FriendCountDto> getFriendCount(
        @Parameter(hidden = true) Principal principal) {
        return ResponseEntity.ok(friendService.getFriendCount(principal.getName()));
    }

    @PostMapping
    @Operation(summary = "Add a new friend")
    public ResponseEntity<FriendResponseDto> addFriend(
        @Parameter(hidden = true) Principal principal,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Friend request details",
            required = true,
            content = @Content(schema = @Schema(implementation = FriendRequestDto.class))
        )
        @Valid @RequestBody FriendRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(friendService.addFriend(principal.getName(), requestDto.getFriendEmail()));
    }

    @DeleteMapping
    @Operation(summary = "Remove a friend")
    public ResponseEntity<Void> removeFriend(
        @Parameter(hidden = true) Principal principal,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Friend request details",
            required = true,
            content = @Content(schema = @Schema(implementation = FriendRequestDto.class))
        )
        @RequestBody FriendRequestDto requestDto) {
        friendService.removeFriend(principal.getName(), requestDto.getFriendEmail());
        return ResponseEntity.noContent().build();
    }
}
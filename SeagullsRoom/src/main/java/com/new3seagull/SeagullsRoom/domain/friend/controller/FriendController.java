package com.new3seagull.SeagullsRoom.domain.friend.controller;

import com.new3seagull.SeagullsRoom.domain.friend.dto.FriendCountDto;
import com.new3seagull.SeagullsRoom.domain.friend.dto.FriendRequestDto;
import com.new3seagull.SeagullsRoom.domain.friend.dto.FriendResponseDto;
import com.new3seagull.SeagullsRoom.domain.friend.service.FriendService;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/friend")
public class FriendController {

    private final FriendService friendService;

    @GetMapping
    public ResponseEntity<List<FriendResponseDto>> getFriends(Principal principal) {
        return ResponseEntity.ok(friendService.getFriendsByUserEmail(principal.getName()));
    }

    @GetMapping("/count")
    public ResponseEntity<FriendCountDto> getFriendCount(Principal principal) {
        return ResponseEntity.ok(friendService.getFriendCount(principal.getName()));
    }

    @PostMapping
    public ResponseEntity<FriendResponseDto> addFriend(Principal principal,
        @RequestBody FriendRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(friendService.addFriend(principal.getName(), requestDto.getFriendEmail()));
    }

    @DeleteMapping
    public ResponseEntity<Void> removeFriend(Principal principal, @RequestBody FriendRequestDto requestDto) {
        friendService.removeFriend(principal.getName(), requestDto.getFriendEmail());
        return ResponseEntity.noContent().build();
    }
}
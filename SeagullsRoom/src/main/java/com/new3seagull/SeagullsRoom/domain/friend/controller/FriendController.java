package com.new3seagull.SeagullsRoom.domain.friend.controller;

import com.new3seagull.SeagullsRoom.domain.friend.dto.FriendCountDto;
import com.new3seagull.SeagullsRoom.domain.friend.dto.FriendRequestDto;
import com.new3seagull.SeagullsRoom.domain.friend.dto.FriendResponseDto;
import com.new3seagull.SeagullsRoom.domain.friend.service.FriendService;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/friend")
public class FriendController {

    private final FriendService friendService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<FriendResponseDto> getFriends(Principal principal) {
        return friendService.getFriendsByUserEmail(principal.getName());
    }

    @GetMapping("/count")
    @ResponseStatus(HttpStatus.OK)
    public FriendCountDto getFriendCount(Principal principal) {
        return friendService.getFriendCount(principal.getName());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FriendResponseDto addFriend(Principal principal,
        @RequestBody FriendRequestDto requestDto) {
        return friendService.addFriend(principal.getName(), requestDto.getFriendEmail());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFriend(Principal principal, @RequestBody FriendRequestDto requestDto) {
        friendService.removeFriend(principal.getName(), requestDto.getFriendEmail());
    }
}
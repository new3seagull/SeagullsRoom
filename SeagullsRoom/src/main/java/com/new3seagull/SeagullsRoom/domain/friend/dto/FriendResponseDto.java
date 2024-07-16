package com.new3seagull.SeagullsRoom.domain.friend.dto;

import com.new3seagull.SeagullsRoom.domain.friend.entity.Friend;
import lombok.Builder;

@Builder
public class FriendResponseDto {

    private String friendEmail;
    private String friendName;

    public FriendResponseDto() {
    }

    public FriendResponseDto(String friendEmail, String friendName) {
        this.friendEmail = friendEmail;
        this.friendName = friendName;
    }

    public String getFriendEmail() {
        return friendEmail;
    }

    public String getFriendName() {
        return friendName;
    }

    public static FriendResponseDto toResponseDto(Friend friend) {
        return FriendResponseDto
            .builder()
            .friendEmail(friend.getFriend().getEmail())
            .friendName(friend.getFriend().getName())
            .build();
    }
}

package com.new3seagull.SeagullsRoom.domain.friend.dto;

public class FriendResponseDto {

    private Long friendId;
    private String friendName;

    public FriendResponseDto() {
    }

    public FriendResponseDto(Long friendId, String friendName) {
        this.friendId = friendId;
        this.friendName = friendName;
    }

    public Long getFriendId() {
        return friendId;
    }

    public String getFriendName() {
        return friendName;
    }
}

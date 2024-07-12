package com.new3seagull.SeagullsRoom.domain.friend.dto;

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
}

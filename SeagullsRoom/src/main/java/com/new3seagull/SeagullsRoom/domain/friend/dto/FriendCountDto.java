package com.new3seagull.SeagullsRoom.domain.friend.dto;

import lombok.Getter;

@Getter
public class FriendCountDto {
    private long followingCount;
    private long followerCount;

    public FriendCountDto(long followingCount, long followerCount) {
        this.followingCount = followingCount;
        this.followerCount = followerCount;
    }
}
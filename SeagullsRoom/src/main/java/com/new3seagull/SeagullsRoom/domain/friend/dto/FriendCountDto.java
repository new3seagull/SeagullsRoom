package com.new3seagull.SeagullsRoom.domain.friend.dto;

public class FriendCountDto {
    private long followingCount;
    private long followerCount;

    public FriendCountDto(long followingCount, long followerCount) {
        this.followingCount = followingCount;
        this.followerCount = followerCount;
    }

    public long getFollowingCount() {
        return followingCount;
    }

    public long getFollowerCount() {
        return followerCount;
    }
}
package com.new3seagull.SeagullsRoom.domain.user.dto;

import com.new3seagull.SeagullsRoom.domain.user.entity.User;

public record UserDto(String email, String password, String role) {
    public static UserDto toDto(User user) {
        return new UserDto(user.getEmail(), user.getPassword(), user.getRole());
    }
}

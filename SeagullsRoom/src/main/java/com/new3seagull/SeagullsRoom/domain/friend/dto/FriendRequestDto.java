package com.new3seagull.SeagullsRoom.domain.friend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequestDto {
    @NotBlank(message = "친구의 이메일은 필수입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String friendEmail;
}
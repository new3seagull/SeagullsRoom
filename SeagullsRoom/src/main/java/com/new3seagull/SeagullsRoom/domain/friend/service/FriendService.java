package com.new3seagull.SeagullsRoom.domain.friend.service;

import com.new3seagull.SeagullsRoom.domain.friend.dto.FriendResponseDto;
import com.new3seagull.SeagullsRoom.domain.friend.entity.Friend;
import com.new3seagull.SeagullsRoom.domain.friend.repository.FriendRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;

    @Transactional(readOnly = true)
    public List<FriendResponseDto> getFriendsByUserEmail(String email) {
        List<Friend> friends = friendRepository.findAllByUserEmail(email);
        return friends.stream()
            .map(Friend::toResponseDto)
            .collect(Collectors.toList());
    }
}
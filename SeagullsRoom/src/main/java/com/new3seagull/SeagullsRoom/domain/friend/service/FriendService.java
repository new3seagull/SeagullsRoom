package com.new3seagull.SeagullsRoom.domain.friend.service;

import com.new3seagull.SeagullsRoom.domain.friend.dto.FriendResponseDto;
import com.new3seagull.SeagullsRoom.domain.friend.entity.Friend;
import com.new3seagull.SeagullsRoom.domain.friend.repository.FriendRepository;
import com.new3seagull.SeagullsRoom.domain.user.entity.User;
import com.new3seagull.SeagullsRoom.domain.user.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<FriendResponseDto> getFriendsByUserEmail(String email) {
        List<Friend> friends = friendRepository.findAllByUserEmail(email);
        return friends.stream()
            .map(Friend::toResponseDto)
            .collect(Collectors.toList());
    }

    @Transactional
    public FriendResponseDto addFriend(String email, String friendEmail) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        User friend = userRepository.findByEmail(friendEmail)
            .orElseThrow(() -> new RuntimeException("해당 사용자를 찾을 수 없습니다."));

        // 자기 자신을 친구로 추가하려는 경우
        if (user.getId().equals(friend.getId())) {
            throw new IllegalArgumentException("자기 자신을 친구로 등록할 수 없습니다.");
        }

        // 이미 친구 관계인지 확인
        if (friendRepository.existsByUserAndFriend(user, friend)) {
            throw new IllegalStateException("이미 등록된 친구입니다.");
        }

        Friend newFriend = new Friend();
        newFriend.setUser(user);
        newFriend.setFriend(friend);

        Friend savedFriend = friendRepository.save(newFriend);

        return new FriendResponseDto(savedFriend.getFriend().getId(), savedFriend.getFriend().getName());
    }
}
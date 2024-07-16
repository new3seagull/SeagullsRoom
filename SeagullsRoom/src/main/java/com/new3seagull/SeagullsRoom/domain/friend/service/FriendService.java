package com.new3seagull.SeagullsRoom.domain.friend.service;

import com.new3seagull.SeagullsRoom.domain.friend.dto.FriendCountDto;
import com.new3seagull.SeagullsRoom.domain.friend.dto.FriendResponseDto;
import com.new3seagull.SeagullsRoom.domain.friend.entity.Friend;
import com.new3seagull.SeagullsRoom.domain.friend.repository.FriendRepository;
import com.new3seagull.SeagullsRoom.domain.user.entity.User;
import com.new3seagull.SeagullsRoom.domain.user.repository.UserRepository;
import com.new3seagull.SeagullsRoom.domain.user.service.UserService;
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
    private final UserService userService;

    @Transactional(readOnly = true)
    public List<FriendResponseDto> getFriendsByUserEmail(String email) {
        List<Friend> friends = friendRepository.findAllByUserEmail(email);
        return friends.stream()
            .map(FriendResponseDto::toResponseDto)
            .collect(Collectors.toList());
    }

    @Transactional
    public FriendResponseDto addFriend(String userEmail, String friendEmail) {
        User user = userService.getUserByEmail(userEmail);

        User friend = userService.getUserByEmail(friendEmail);

        // 자기 자신을 친구로 추가하려는 경우
        if (user.getId().equals(friend.getId())) {
            throw new IllegalArgumentException("자기 자신을 친구로 등록할 수 없습니다.");
        }

        // 이미 친구 관계인지 확인
        if (friendRepository.existsByUserAndFriend(user, friend)) {
            throw new IllegalStateException("이미 등록된 친구입니다.");
        }

        Friend newFriend = Friend.builder()
            .user(user)
            .friend(friend)
            .build();

        return FriendResponseDto.toResponseDto(friendRepository.save(newFriend));
    }

    @Transactional
    public void removeFriend(String userEmail, String friendEmail) {
        User user = userService.getUserByEmail(userEmail);

        User friend = userService.getUserByEmail(friendEmail);

        Friend friendRelation = friendRepository.findByUserAndFriend(user, friend)
            .orElseThrow(() -> new RuntimeException("등록된 친구를 찾을 수 없습니다."));

        friendRepository.delete(friendRelation);
    }

    @Transactional(readOnly = true)
    public FriendCountDto getFriendCount(String email) {
        User user = userService.getUserByEmail(email);

        long followingCount = friendRepository.countByUser(user);
        long followerCount = friendRepository.countByFriend(user);

        return new FriendCountDto(followingCount, followerCount);
    }
}
package com.new3seagull.SeagullsRoom.domain.friend.service;

import com.new3seagull.SeagullsRoom.domain.friend.repository.FriendRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
}

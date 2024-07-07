package com.new3seagull.SeagullsRoom.domain.friend.repository;

import com.new3seagull.SeagullsRoom.domain.friend.entity.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {

}

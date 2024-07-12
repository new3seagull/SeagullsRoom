package com.new3seagull.SeagullsRoom.domain.friend.repository;

import com.new3seagull.SeagullsRoom.domain.friend.entity.Friend;
import com.new3seagull.SeagullsRoom.domain.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRepository extends JpaRepository<Friend, Long> {
    List<Friend> findAllByUserEmail(String email);
    boolean existsByUserAndFriend(User user, User friend);
}

package com.new3seagull.SeagullsRoom.domain.friend.entity;

import com.new3seagull.SeagullsRoom.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "friends",
    uniqueConstraints = @UniqueConstraint(name = "uk_user_friend", columnNames = {"user_id", "friend_id"}))
@Getter
@NoArgsConstructor
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friend_id", nullable = false)
    private User friend;

    @Builder
    private Friend(User user, User friend) {
        this.user = user;
        this.friend = friend;
    }
}
package com.new3seagull.SeagullsRoom.domain.friend.entity;

import com.new3seagull.SeagullsRoom.domain.friend.dto.FriendResponseDto;
import com.new3seagull.SeagullsRoom.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "friends",
    uniqueConstraints = @UniqueConstraint(name = "uk_user_friend", columnNames = {"user_id", "friend_id"}))
@Getter
@Setter
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

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public FriendResponseDto toResponseDto() {
        return new FriendResponseDto(this.friend.getId(), this.friend.getName());
    }
}
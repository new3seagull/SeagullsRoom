package com.new3seagull.SeagullsRoom.domain.screenTime.entity;

import com.new3seagull.SeagullsRoom.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Getter
@Table(name = "screen_time")
@NoArgsConstructor
public class ScreenTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private Integer count;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    private ScreenTime(User user, String category, Integer count, LocalDateTime updatedAt) {
        this.user = user;
        this.category = category;
        this.count = count;
        this.updatedAt = updatedAt;
    }
}

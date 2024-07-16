package com.new3seagull.SeagullsRoom.domain.study.entity;

import com.new3seagull.SeagullsRoom.domain.study.dto.StudyResponseDto;
import com.new3seagull.SeagullsRoom.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
public class Study {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "studytime")
    private LocalTime studyTime;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Study() {
    }

    @Builder
    private Study(User user, LocalTime studyTime) {
        this.user = user;
        this.studyTime = studyTime;
    }


}
package com.new3seagull.SeagullsRoom.domain.study.entity;

import com.new3seagull.SeagullsRoom.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor
public class Study {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "study_time")
    private LocalTime studyTime;

    @CreationTimestamp
    @Column(name = "study_date", updatable = false)
    private LocalDate studyDate;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    private Study(User user, Long id, LocalTime studyTime, LocalDate studyDate, LocalDateTime updatedAt) {
        this.user = user;
        this.studyTime = studyTime;
        this.studyDate = studyDate;
        this.updatedAt = updatedAt;
    }
}
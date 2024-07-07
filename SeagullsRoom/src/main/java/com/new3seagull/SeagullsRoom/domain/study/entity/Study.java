package com.new3seagull.SeagullsRoom.domain.study.entity;

import com.new3seagull.SeagullsRoom.domain.study.dto.StudyResponseDto;
import com.new3seagull.SeagullsRoom.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalTime;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Study {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "studytime")
    private LocalTime studyTime;

    public StudyResponseDto toDto() {
        return new StudyResponseDto(this.id, this.user.getEmail(), this.studyTime);
    }
}
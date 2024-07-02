package com.new3seagull.SeagullsRoom.domain.study.service;

import com.new3seagull.SeagullsRoom.domain.study.entity.Study;
import com.new3seagull.SeagullsRoom.domain.study.repository.StudyRepository;
import com.new3seagull.SeagullsRoom.domain.user.entity.User;
import jakarta.transaction.Transactional;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;

    @Transactional
    public LocalTime getStudytimeByUserId(User userId) {
        List<Study> userStudies = studyRepository.findAllByUserId(userId);

        if (userStudies.isEmpty()) {
            return LocalTime.of(0, 0); // 공부 기록이 없으면 0시간 0분 반환
        }

        long totalSeconds = userStudies.stream()
            .map(Study::getStudyTime)
            .mapToLong(time -> time.toSecondOfDay())
            .sum();

        return LocalTime.ofSecondOfDay(totalSeconds);
    }
}

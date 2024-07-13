package com.new3seagull.SeagullsRoom.domain.study.service;

import com.new3seagull.SeagullsRoom.domain.study.entity.Study;
import com.new3seagull.SeagullsRoom.domain.study.repository.StudyRepository;
import com.new3seagull.SeagullsRoom.domain.user.entity.User;
import com.new3seagull.SeagullsRoom.global.error.isNotUsersStudyException;
import jakarta.persistence.EntityNotFoundException;

import java.time.LocalTime;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class StudyService {

    private final StudyRepository studyRepository;

    public List<Study> getStudytimesByUser(User user) {
        return studyRepository.findAllByUser(user);
    }

    public Study getStudyById(User user, Long id) {
        Study study = studyRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Study not found with id: " + id));

        if (!study.getUser().equals(user)) {
            throw new isNotUsersStudyException("You don't have permission to access this study");
        }

        return study;
    }

    @Transactional
    public Study recordStudyTime(User user, LocalTime studyTime) {

        return studyRepository.save(Study.builder()
                .user(user)
                .studyTime(studyTime)
                .build());
    }
}

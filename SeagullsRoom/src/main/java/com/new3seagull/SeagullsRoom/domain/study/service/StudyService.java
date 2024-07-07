package com.new3seagull.SeagullsRoom.domain.study.service;

import com.new3seagull.SeagullsRoom.domain.study.entity.Study;
import com.new3seagull.SeagullsRoom.domain.study.repository.StudyRepository;
import com.new3seagull.SeagullsRoom.domain.user.entity.User;
import com.new3seagull.SeagullsRoom.global.error.isNotUsersStudyException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.time.LocalTime;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;

    @Transactional
    public List<Study> getStudytimesByUser(User user) {
        return studyRepository.findAllByUser(user);
        // todo - id와 studyTime만?
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
        Study study = new Study();
        study.setUser(user);
        study.setStudyTime(studyTime);
        return studyRepository.save(study);
    }
}

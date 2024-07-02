package com.new3seagull.SeagullsRoom.domain.study.service;

import com.new3seagull.SeagullsRoom.domain.study.entity.Study;
import com.new3seagull.SeagullsRoom.domain.study.repository.StudyRepository;
import com.new3seagull.SeagullsRoom.domain.user.entity.User;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;

    @Transactional
    public List<Study> getStudytimesByUserId(User user) {
        return studyRepository.findAllByUser(user);
        // todo - id와 studyTime만?
    }
}

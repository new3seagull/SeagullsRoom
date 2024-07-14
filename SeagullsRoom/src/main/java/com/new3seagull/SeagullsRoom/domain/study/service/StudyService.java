package com.new3seagull.SeagullsRoom.domain.study.service;

import static com.new3seagull.SeagullsRoom.global.error.ExceptionCode.NOT_USERS_STUDY;

import com.new3seagull.SeagullsRoom.domain.study.dto.StudyResponseDto;
import com.new3seagull.SeagullsRoom.domain.study.entity.Study;
import com.new3seagull.SeagullsRoom.domain.study.repository.StudyRepository;
import com.new3seagull.SeagullsRoom.domain.user.entity.User;
import com.new3seagull.SeagullsRoom.global.error.CustomException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;

    @Transactional
    public List<StudyResponseDto> getStudytimesByUser(User user) {
        List<Study> studies = studyRepository.findAllByUser(user);
        List<StudyResponseDto> responseDtos = studies.stream()
            .map(Study::toDto)
            .collect(Collectors.toList());
        return responseDtos;
    }

    public Study getStudyById(User user, Long id) {
        Study study = studyRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Study not found with id: " + id));

        if (!study.getUser().equals(user)) {
            throw new CustomException(NOT_USERS_STUDY);
        }

        return study;
    }

    @Transactional
    public Study recordStudyTime(User user, LocalTime studyTime) {
        Study study = new Study(user, studyTime);
        return studyRepository.save(study);
    }
}

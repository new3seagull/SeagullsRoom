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
        return studies.stream()
            .map(StudyResponseDto::toDto)
            .collect(Collectors.toList());
    }

    public StudyResponseDto getStudyById(User user, Long id) {
        Study study = studyRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Study not found with id: " + id));

        if (!study.getUser().equals(user)) {
            throw new CustomException(NOT_USERS_STUDY);
        }

        return StudyResponseDto.toDto(study);
    }

    @Transactional
    public StudyResponseDto recordStudyTime(User user, LocalTime studyTime) {
        Study study = Study.builder().user(user).studyTime(studyTime).build();
        return StudyResponseDto.toDto(studyRepository.save(study));
    }
}

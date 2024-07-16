package com.new3seagull.SeagullsRoom.domain.study.service;

import static com.new3seagull.SeagullsRoom.global.error.ExceptionCode.NOT_USERS_STUDY;
import com.new3seagull.SeagullsRoom.domain.study.dto.StudyResponseDto;
import com.new3seagull.SeagullsRoom.domain.study.entity.Study;
import com.new3seagull.SeagullsRoom.domain.study.repository.StudyRepository;
import com.new3seagull.SeagullsRoom.domain.user.entity.User;
import com.new3seagull.SeagullsRoom.global.error.CustomException;
import com.new3seagull.SeagullsRoom.domain.user.repository.UserRepository;
import com.new3seagull.SeagullsRoom.global.error.CustomException;
import com.new3seagull.SeagullsRoom.global.error.isNotUsersStudyException;
import jakarta.persistence.EntityNotFoundException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.new3seagull.SeagullsRoom.global.error.ExceptionCode.USER_NOT_FOUND;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class StudyService {

    private final StudyRepository studyRepository;
    private final UserRepository userRepository;

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
    public Study recordStudyTime(User user, LocalTime studyTime) {

        return studyRepository.save(Study.builder()
                .user(user)
                .studyTime(studyTime)
                .build());
    }


    public List<StudyResponseDto> getTop10StudyTimes(LocalDate date) {
        Pageable pageable = PageRequest.of(0, 10);
        List<Study> top10Studies = studyRepository.findAllByStudyDateOrderByStudyTimeDesc(pageable,date).getContent();
        return top10Studies.stream()
                .map(StudyResponseDto::toDto)
                .collect(Collectors.toList());
    }
    // 유저의 특정 날짜의 공부 시간 조회
    public StudyResponseDto getStudyTimeByDate(Principal principal, LocalDate date) {
        User user = userRepository.findByEmail(principal.getName());
        if (user == null) {
            throw new CustomException(USER_NOT_FOUND);
        }
        Study study = studyRepository.findByUserAndStudyDate(user, date);
        return StudyResponseDto.toDto(study);
    }

    public List<StudyResponseDto> getStudyTimeByMonth(Principal principal, LocalDate date) {
        User user = userRepository.findByEmail(principal.getName());
        if (user == null) {
            throw new CustomException(USER_NOT_FOUND);
        }
        List<Study> study = studyRepository.findByUserAndYearAndMonth(user, date.getYear(), date.getMonthValue());

        return study.stream()
                .map(StudyResponseDto::toDto)
                .collect(Collectors.toList());
    }
}

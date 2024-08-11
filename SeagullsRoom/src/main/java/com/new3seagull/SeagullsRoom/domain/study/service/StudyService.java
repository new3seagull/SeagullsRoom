package com.new3seagull.SeagullsRoom.domain.study.service;

import static com.new3seagull.SeagullsRoom.global.error.ExceptionCode.NOT_USERS_STUDY;

import com.new3seagull.SeagullsRoom.domain.study.dto.StudyResponseDto;
import com.new3seagull.SeagullsRoom.domain.study.entity.Study;
import com.new3seagull.SeagullsRoom.domain.study.repository.StudyRepository;
import com.new3seagull.SeagullsRoom.domain.user.entity.User;
import com.new3seagull.SeagullsRoom.global.error.CustomException;
import com.new3seagull.SeagullsRoom.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
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
    public StudyResponseDto recordStudyTime(User user, LocalTime studyTime) {

        return StudyResponseDto.toDto(studyRepository.save(Study.builder()
            .user(user)
            .studyTime(studyTime)
            .build()));
    }

    public List<StudyResponseDto> getTop10StudyTimes(LocalDate date) {
        Pageable pageable = PageRequest.of(0, 10);
        List<Object[]> top10 = studyRepository.findStudyTimeRankingByDate(
                date.getYear(), date.getMonthValue(), date.getDayOfMonth(), pageable).getContent();

        return top10.stream()
                .map(row -> {
                    User user = (User) row[0];
                    int totalStudyTimeInMinutes = ((Long) row[1]).intValue(); // 총 공부 시간을 분 단위로 받아옴

                    // 분 단위 시간을 LocalTime으로 변환
                    LocalTime studyTime = convertSecondsToLocalTime(totalStudyTimeInMinutes);

                    // 현재 시간을 updatedAt으로 설정
                    LocalDateTime updatedAt = LocalDateTime.now();

                    // StudyResponseDto 객체 생성 및 반환
                    return StudyResponseDto.builder()
                            .userEmail(user.getEmail())
                            .studyTime(studyTime)
                            .updatedAt(updatedAt)
                            .build();
                })
                .collect(Collectors.toList());
    }

    // 유저의 특정 날짜의 공부 시간 조회
    public StudyResponseDto getStudyTimeByDate(Principal principal, LocalDate date) {
        User user = userRepository.findByEmail(principal.getName());
        if (user == null) {
            throw new CustomException(USER_NOT_FOUND);
        }

        // StudyRepository에서 해당 날짜의 모든 공부 시간을 가져옴
        List<LocalTime> totalStudyTimeByUserIdAndDate = studyRepository.findTotalStudyTimeByUserIdAndDate(
                user, date.getYear(), date.getMonthValue(), date.getDayOfMonth());

        // 모든 LocalTime의 분을 합산
        int totalSeconds = totalStudyTimeByUserIdAndDate.stream()
                .mapToInt(time -> time.getHour() * 3600 + time.getMinute() * 60 + time.getSecond())
                .sum();


        // LocalDate와 LocalTime을 결합하여 LocalDateTime 생성
        return StudyResponseDto.builder()
                .studyTime(convertSecondsToLocalTime(totalSeconds))
                .userEmail(user.getEmail())
                .build();
    }

    public List<StudyResponseDto> getStudyTimeByMonth(Principal principal, LocalDate date) {
        User user = userRepository.findByEmail(principal.getName());
        if (user == null) {
            throw new CustomException(USER_NOT_FOUND);
        }
        List<Study> study = studyRepository.findByUserAndYearAndMonth(user, date.getYear(),
            date.getMonthValue());

        return study.stream()
            .map(StudyResponseDto::toDto)
            .collect(Collectors.toList());
    }

    private LocalTime convertSecondsToLocalTime(int totalSeconds) {
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;

        return LocalTime.of(hours, minutes, seconds);
    }
}

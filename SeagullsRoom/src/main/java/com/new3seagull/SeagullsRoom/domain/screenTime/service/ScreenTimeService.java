package com.new3seagull.SeagullsRoom.domain.screenTime.service;

import static com.new3seagull.SeagullsRoom.global.error.ExceptionCode.USER_NOT_FOUND;

import com.new3seagull.SeagullsRoom.domain.screenTime.dto.ScreenTimeRequestDto;
import com.new3seagull.SeagullsRoom.domain.screenTime.dto.ScreenTimeResponseDto;
import com.new3seagull.SeagullsRoom.domain.screenTime.entity.ScreenTime;
import com.new3seagull.SeagullsRoom.domain.screenTime.repository.ScreenTimeRepository;
import com.new3seagull.SeagullsRoom.domain.user.entity.User;
import com.new3seagull.SeagullsRoom.domain.user.repository.UserRepository;
import com.new3seagull.SeagullsRoom.global.error.CustomException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScreenTimeService {

    private final ScreenTimeRepository screenTimeRepository;
    private final UserRepository userRepository;

    public List<ScreenTimeResponseDto> getScreenTime(String email) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new CustomException(USER_NOT_FOUND);
        }

        // 사용자의 전체 스크린 타임 조회
        List<Object[]> results = screenTimeRepository.findScreenTimesByUserGroupByCategory(user);

        return results.stream().map(result -> {
            String category = (String) result[0];
            Long totalCount = (Long) result[1];
            return ScreenTimeResponseDto.builder()
                .category(category)
                .count(totalCount.intValue())
                .build();
        }).collect(Collectors.toList());
    }

    public ScreenTimeResponseDto addScreenTime(String email, ScreenTimeRequestDto requestDto) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new CustomException(USER_NOT_FOUND);
        }

        ScreenTime screenTime = ScreenTime.builder()
            .user(user)
            .count(requestDto.getCount())
            .category(requestDto.getCategory())
            .updatedAt(LocalDateTime.now())
            .build();

        ScreenTime savedScreenTime = screenTimeRepository.save(screenTime);
        return ScreenTimeResponseDto.toDto(savedScreenTime);
    }
}

package com.new3seagull.SeagullsRoom.domain.user.service;

import com.new3seagull.SeagullsRoom.domain.user.dto.JoinDto;
import com.new3seagull.SeagullsRoom.domain.user.entity.User;
import com.new3seagull.SeagullsRoom.domain.user.repository.UserRepository;
import com.new3seagull.SeagullsRoom.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.new3seagull.SeagullsRoom.global.error.ExceptionCode.USER_EMAIL_ALREADY_EXISTS;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void joinProcess(JoinDto joinDTO) {


        Boolean isExist = userRepository.existsByEmail(joinDTO.email());

        if (isExist) { // 이메일 중복 검사
            throw new CustomException(USER_EMAIL_ALREADY_EXISTS);
        }

        User newUser = User.builder()
                .name(joinDTO.name())
                .email(joinDTO.email())
                .password(bCryptPasswordEncoder.encode(joinDTO.password()))
                .role("ROLE_USER")
                .build();

        userRepository.save(newUser);
    }

}

package com.new3seagull.SeagullsRoom.domain.user.service;

import com.new3seagull.SeagullsRoom.domain.user.dto.JoinDto;
import com.new3seagull.SeagullsRoom.domain.user.entity.User;
import com.new3seagull.SeagullsRoom.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class JoinService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void joinProcess(JoinDto joinDTO) {


        Boolean isExist = userRepository.existsByEmail(joinDTO.email());

        if (isExist) {

            return;
        }

        User newUser = User.builder()
                .name(joinDTO.name())
                .email(joinDTO.email())
                .password(bCryptPasswordEncoder.encode(joinDTO.password()))
                .role("ROLE_ADMIN")
                .build();

        userRepository.save(newUser);
    }

}

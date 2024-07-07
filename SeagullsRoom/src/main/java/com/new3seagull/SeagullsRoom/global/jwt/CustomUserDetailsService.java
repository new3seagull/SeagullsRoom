package com.new3seagull.SeagullsRoom.global.jwt;

import com.new3seagull.SeagullsRoom.domain.user.dto.UserDto;
import com.new3seagull.SeagullsRoom.domain.user.entity.User;
import com.new3seagull.SeagullsRoom.domain.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    //loadUserByUsername에서 데이터베이스에서의 유저를 load해 와서 검증한다.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("loadUserByUsername " + username);
        //DB에서 조회
        User user = userRepository.findUserByEmail(username);

        if (user != null) {

            //UserDetails에 담아서 return하면 AutneticationManager가 검증 함
            return new CustomUserDetails(UserDto.toDto(user));
        }

        return null;
    }
}

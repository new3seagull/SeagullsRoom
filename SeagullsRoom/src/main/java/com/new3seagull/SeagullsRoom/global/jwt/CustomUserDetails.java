package com.new3seagull.SeagullsRoom.global.jwt;

import com.new3seagull.SeagullsRoom.domain.user.dto.UserDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class CustomUserDetails implements UserDetails {

    private final UserDto userDto;

    public CustomUserDetails(UserDto userDto) {

        this.userDto = userDto;
    }


    // 유저의 권한을 반환
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {

            @Override
            public String getAuthority() {

                return userDto.role();
            }
        });

        return collection;
    }

    @Override
    public String getPassword() {

        return userDto.password();
    }

    @Override
    public String getUsername() {

        return userDto.email();
    }

    // 특정 권한이 만료 되었는지
    @Override
    public boolean isAccountNonExpired() {

        return true;
    }

    // 계정이 Block되었는지
    @Override
    public boolean isAccountNonLocked() {

        return true;
    }


    @Override
    public boolean isCredentialsNonExpired() {

        return true;
    }

    @Override
    public boolean isEnabled() {

        return true;
    }
}

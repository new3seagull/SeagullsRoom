package com.new3seagull.SeagullsRoom.domain.user.repository;

import com.new3seagull.SeagullsRoom.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByEmail(String email);

    User findUserByEmail(String email);

    User findByEmail(String email);
}

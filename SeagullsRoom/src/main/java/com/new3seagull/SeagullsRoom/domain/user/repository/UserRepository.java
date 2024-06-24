package com.new3seagull.SeagullsRoom.domain.user.repository;

import com.new3seagull.SeagullsRoom.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}

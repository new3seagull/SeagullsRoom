package com.new3seagull.SeagullsRoom.domain.todo.repository;

import com.new3seagull.SeagullsRoom.domain.todo.entity.Todo;
import com.new3seagull.SeagullsRoom.domain.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    List<Todo> findByUser(User user);

    boolean existsByTitleAndUser(String title, User user);
}

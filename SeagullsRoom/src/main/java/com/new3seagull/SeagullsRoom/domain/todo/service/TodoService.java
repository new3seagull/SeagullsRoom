package com.new3seagull.SeagullsRoom.domain.todo.service;

import static com.new3seagull.SeagullsRoom.global.error.ExceptionCode.DUPLICATE_TODO_TITLE;
import static com.new3seagull.SeagullsRoom.global.error.ExceptionCode.NOT_USERS_TODO;
import static com.new3seagull.SeagullsRoom.global.error.ExceptionCode.NO_TODO;
import static com.new3seagull.SeagullsRoom.global.error.ExceptionCode.USER_NOT_FOUND;

import com.new3seagull.SeagullsRoom.domain.todo.dto.TodoRequestDto;
import com.new3seagull.SeagullsRoom.domain.todo.dto.TodoResponseDto;
import com.new3seagull.SeagullsRoom.domain.todo.entity.Todo;
import com.new3seagull.SeagullsRoom.domain.todo.repository.TodoRepository;
import com.new3seagull.SeagullsRoom.domain.user.entity.User;
import com.new3seagull.SeagullsRoom.domain.user.repository.UserRepository;
import com.new3seagull.SeagullsRoom.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<TodoResponseDto> getTodosByUser(String email) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new CustomException(USER_NOT_FOUND);
        }

        return todoRepository.findByUser(user).stream()
            .map(TodoResponseDto::toDto)
            .collect(Collectors.toList());
    }

    @Transactional
    public TodoResponseDto addTodo(String email, TodoRequestDto requestDto) {
        User user = userRepository.findByEmail(email);

        if (todoRepository.existsByTitleAndUser(requestDto.getTitle(), user)) {
            throw new CustomException(DUPLICATE_TODO_TITLE);
        }

        Todo todo = Todo.builder()
            .title(requestDto.getTitle())
            .description(requestDto.getDescription())
            .completed(requestDto.getCompleted())
            .user(user)
            .build();

        Todo savedTodo = todoRepository.save(todo);
        return TodoResponseDto.toDto(savedTodo);
    }

    @Transactional
    public TodoResponseDto updateTodo(String email, Long id, TodoRequestDto todoRequestDto) {
        Todo todo = todoRepository.findById(id)
            .orElseThrow(() -> new CustomException(NO_TODO));

        // 사용자 소유의 TODO가 아닌 경우 예외 처리
        if (!todo.getUser().getEmail().equals(email)) {
            throw new CustomException(NOT_USERS_TODO);
        }

        todo.update(todoRequestDto.getTitle(), todoRequestDto.getDescription(), todoRequestDto.getCompleted());
        Todo updatedTodo = todoRepository.save(todo);
        return TodoResponseDto.toDto(updatedTodo);
    }

    @Transactional
    public void removeTodo(String email, Long id) {
        Todo todo = todoRepository.findById(id)
            .orElseThrow(() -> new CustomException(NO_TODO));

        // 사용자 소유의 TODO가 아닌 경우 예외 처리
        if (!todo.getUser().getEmail().equals(email)) {
            throw new CustomException(NOT_USERS_TODO);
        }

        todoRepository.delete(todo);
    }
}
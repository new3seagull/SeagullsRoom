package com.new3seagull.SeagullsRoom.domain.todo.controller;

import com.new3seagull.SeagullsRoom.domain.todo.dto.TodoRequestDto;
import com.new3seagull.SeagullsRoom.domain.todo.dto.TodoResponseDto;
import com.new3seagull.SeagullsRoom.domain.todo.service.TodoService;
import jakarta.validation.Valid;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/todo")
public class TodoController {

    private final TodoService todoService;

    @GetMapping
    public ResponseEntity<List<TodoResponseDto>> getTodos(Principal principal) {
        return ResponseEntity.ok(todoService.getTodosByUser(principal.getName()));
    }

    @PostMapping
    public ResponseEntity<TodoResponseDto> addTodo(Principal principal,
        @Valid @RequestBody TodoRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(todoService.addTodo(principal.getName(), requestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TodoResponseDto> updateTodo(Principal principal, @PathVariable Long id,
        @Valid @RequestBody TodoRequestDto todoRequestDto) {
        return ResponseEntity.ok(todoService.updateTodo(principal.getName(), id, todoRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeTodo(Principal principal, @PathVariable Long id) {
        todoService.removeTodo(principal.getName(), id);
        return ResponseEntity.noContent().build();
    }
}

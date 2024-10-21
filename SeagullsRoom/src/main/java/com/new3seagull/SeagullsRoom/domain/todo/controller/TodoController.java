package com.new3seagull.SeagullsRoom.domain.todo.controller;

import com.new3seagull.SeagullsRoom.domain.todo.dto.TodoRequestDto;
import com.new3seagull.SeagullsRoom.domain.todo.dto.TodoResponseDto;
import com.new3seagull.SeagullsRoom.domain.todo.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/todo")
@Tag(name = "Todo", description = "Todo management API")
public class TodoController {

    private final TodoService todoService;

    @GetMapping
    @Operation(summary = "Get all todos", description = "Retrieves all todos for the authenticated user")
    @ApiResponse(responseCode = "200", description = "Successful operation",
        content = @Content(schema = @Schema(implementation = TodoResponseDto.class)))
    public ResponseEntity<List<TodoResponseDto>> getTodos(
        @Parameter(hidden = true) Principal principal) {
        return ResponseEntity.ok(todoService.getTodosByUser(principal.getName()));
    }

    @PostMapping
    @Operation(summary = "Add a new todo", description = "Creates a new todo for the authenticated user")
    @ApiResponse(responseCode = "201", description = "Todo created successfully",
        content = @Content(schema = @Schema(implementation = TodoResponseDto.class)))
    public ResponseEntity<TodoResponseDto> addTodo(
        @Parameter(hidden = true) Principal principal,
        @Parameter(description = "Todo details", required = true) @Valid @RequestBody TodoRequestDto requestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(todoService.addTodo(principal.getName(), requestDto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a todo", description = "Updates an existing todo for the authenticated user")
    @ApiResponse(responseCode = "200", description = "Todo updated successfully",
        content = @Content(schema = @Schema(implementation = TodoResponseDto.class)))
    public ResponseEntity<TodoResponseDto> updateTodo(
        @Parameter(hidden = true) Principal principal,
        @Parameter(description = "ID of the todo to update", required = true) @PathVariable Long id,
        @Parameter(description = "Updated todo details", required = true) @Valid @RequestBody TodoRequestDto todoRequestDto) {
        return ResponseEntity.ok(todoService.updateTodo(principal.getName(), id, todoRequestDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove a todo", description = "Deletes a todo for the authenticated user")
    @ApiResponse(responseCode = "204", description = "Todo deleted successfully")
    public ResponseEntity<Void> removeTodo(
        @Parameter(hidden = true) Principal principal,
        @Parameter(description = "ID of the todo to delete", required = true) @PathVariable Long id) {
        todoService.removeTodo(principal.getName(), id);
        return ResponseEntity.noContent().build();
    }
}
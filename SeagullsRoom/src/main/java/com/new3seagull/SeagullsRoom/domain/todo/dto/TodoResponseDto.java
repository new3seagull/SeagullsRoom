package com.new3seagull.SeagullsRoom.domain.todo.dto;

import com.new3seagull.SeagullsRoom.domain.todo.entity.Todo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodoResponseDto {

    private Long id;
    private String title;
    private String description;
    private boolean completed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static TodoResponseDto toDto(Todo todo) {
        return TodoResponseDto.builder()
            .id(todo.getId())
            .title(todo.getTitle())
            .description(todo.getDescription())
            .completed(todo.isCompleted())
            .createdAt(todo.getCreatedAt())
            .updatedAt(todo.getUpdatedAt())
            .build();
    }
}
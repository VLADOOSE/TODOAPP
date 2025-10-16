package com.vladoose.todoapp.dto;

import com.vladoose.todoapp.util.enums.TaskStatus;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaskDto {
    private Long id;
    @NotBlank
    private String title;
    private String description;
    @FutureOrPresent
    private LocalDate dueDate;
    private TaskStatus status;
}

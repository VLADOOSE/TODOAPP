package com.vladoose.todoapp.mapper;

import com.vladoose.todoapp.dto.TaskDto;
import com.vladoose.todoapp.model.Task;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    TaskDto toDto(Task task);
    Task toEntity(TaskDto dto);

    // Дополнительно: преобразование списков
    List<TaskDto> toDtoList(List<Task> tasks);
}


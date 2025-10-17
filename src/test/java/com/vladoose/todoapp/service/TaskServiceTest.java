package com.vladoose.todoapp.service;

import com.vladoose.todoapp.dto.TaskDto;
import com.vladoose.todoapp.exception.NotFoundException;
import com.vladoose.todoapp.mapper.TaskMapper;
import com.vladoose.todoapp.model.Task;
import com.vladoose.todoapp.repository.TaskRepository;
import com.vladoose.todoapp.util.enums.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    private Task task;
    private TaskDto dto;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        task = new Task();
        task.setId(1L);
        task.setTitle("Test Task");
        task.setDescription("Some description");
        task.setDueDate(LocalDate.of(2025, 10, 10));
        task.setStatus(TaskStatus.TODO);

        dto = new TaskDto();
        dto.setId(1L);
        dto.setTitle("Test Task");
        dto.setDescription("Some description");
        dto.setDueDate(LocalDate.of(2025, 10, 10));
        dto.setStatus(TaskStatus.TODO);
    }


    @Test
    void createTask_shouldSaveAndReturnDto() {
        when(taskMapper.toEntity(dto)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(task);
        when(taskMapper.toDto(task)).thenReturn(dto);

        TaskDto result = taskService.createTask(dto);

        assertNotNull(result);
        assertEquals(dto.getTitle(), result.getTitle());
        verify(taskRepository).save(task);
        verify(taskMapper).toEntity(dto);
        verify(taskMapper).toDto(task);
    }

    @Test
    void editTask_shouldUpdateExistingTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenAnswer(inv -> inv.getArgument(0));


        when(taskMapper.toDto(any(Task.class))).thenAnswer(inv -> {
            Task t = inv.getArgument(0);
            TaskDto d = new TaskDto();
            d.setId(t.getId());
            d.setTitle(t.getTitle());
            d.setDescription(t.getDescription());
            d.setDueDate(t.getDueDate());
            d.setStatus(t.getStatus());
            return d;
        });

        TaskDto updated = new TaskDto();
        updated.setTitle("Updated title");
        updated.setDescription("Updated desc");
        updated.setDueDate(LocalDate.of(2025, 12, 12));
        updated.setStatus(TaskStatus.DONE);

        TaskDto result = taskService.editTask(1L, updated);

        assertEquals("Updated title", result.getTitle());
        assertEquals(TaskStatus.DONE, result.getStatus());
        verify(taskRepository).save(any(Task.class));
    }


    @Test
    void editTask_shouldThrowIfNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> taskService.editTask(99L, dto));
    }

    // --- deleteTask ---
    @Test
    void deleteTask_shouldCallRepositoryDelete() {
        taskService.deleteTask(1L);
        verify(taskRepository).deleteById(1L);
    }


    @Test
    void getTaskById_shouldReturnDtoIfFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(taskMapper.toDto(task)).thenReturn(dto);

        TaskDto result = taskService.getTaskById(1L);

        assertEquals(dto, result);
    }

    @Test
    void getTaskById_shouldThrowIfNotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> taskService.getTaskById(1L));
    }


    @Test
    void getTasks_shouldFilterAndSortCorrectly() {
        when(taskRepository.findByStatus(TaskStatus.TODO, Sort.by(Sort.Direction.ASC, "dueDate")))
                .thenReturn(List.of(task));
        when(taskMapper.toDtoList(List.of(task))).thenReturn(List.of(dto));

        List<TaskDto> result = taskService.getTasks("TODO", "dueDate", "asc");

        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }

    @Test
    void getTasks_shouldReturnAllIfNoStatus() {
        when(taskRepository.findAll(Sort.by(Sort.Direction.DESC, "status")))
                .thenReturn(List.of(task));
        when(taskMapper.toDtoList(List.of(task))).thenReturn(List.of(dto));

        List<TaskDto> result = taskService.getTasks("", "status", "desc");

        assertEquals(1, result.size());
        verify(taskRepository).findAll(any(Sort.class));
    }
}


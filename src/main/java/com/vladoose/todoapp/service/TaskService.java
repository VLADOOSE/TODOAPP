package com.vladoose.todoapp.service;

import com.vladoose.todoapp.dto.TaskDto;
import com.vladoose.todoapp.exception.NotFoundException;
import com.vladoose.todoapp.mapper.TaskMapper;
import com.vladoose.todoapp.model.Task;
import com.vladoose.todoapp.repository.TaskRepository;
import com.vladoose.todoapp.util.enums.TaskStatus;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }
    public List<TaskDto> getTasks(String status, String sortBy, String order) {
        Sort sort = Sort.unsorted();

        if (sortBy != null && !sortBy.isEmpty()) {
            Sort.Direction direction = "desc".equalsIgnoreCase(order) ? Sort.Direction.DESC : Sort.Direction.ASC;
            sort = Sort.by(direction, sortBy);
        }

        List<Task> tasks;

        if (status != null && !status.isEmpty()) {
            TaskStatus taskStatus = TaskStatus.valueOf(status.toUpperCase());
            tasks = taskRepository.findByStatus(taskStatus, sort);
        } else {
            tasks = taskRepository.findAll(sort);
        }

        return taskMapper.toDtoList(tasks);
    }
    public List<TaskDto> getAllTasks(){
        return  taskMapper.toDtoList(taskRepository.findAll());
    }

    public TaskDto getTaskById(Long id){
        if(taskRepository.findById(id).isPresent()){
            return taskMapper.toDto(taskRepository.findById(id).get());
        }else{
            throw new NotFoundException("Таск не найден");
        }
    }
    @Transactional
    public TaskDto createTask(TaskDto dto){
        Task task = taskMapper.toEntity(dto);
        Task savedTask = taskRepository.save(task);
        return taskMapper.toDto(savedTask);
    }
    public TaskDto editTask(Long id, TaskDto dto){
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Таск не найден"));

        existingTask.setTitle(dto.getTitle());
        existingTask.setDescription(dto.getDescription());
        existingTask.setDueDate(dto.getDueDate());
        existingTask.setStatus(dto.getStatus());

        Task savedTask = taskRepository.save(existingTask);
        return taskMapper.toDto(savedTask);
    }

    public void deleteTask(Long id){
        taskRepository.deleteById(id);
    }
}

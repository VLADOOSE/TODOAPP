package com.vladoose.todoapp.repository;

import com.vladoose.todoapp.model.Task;
import com.vladoose.todoapp.util.enums.TaskStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStatus(TaskStatus status);
    List<Task> findByStatus(TaskStatus status, Sort sort);
    List<Task> findAllByOrderByDueDateAsc();


    List<Task> findByDueDate(LocalDate dueDate);
    List<Task> findAll(Sort sort);


}

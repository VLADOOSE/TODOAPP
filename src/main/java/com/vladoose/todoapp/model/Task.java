package com.vladoose.todoapp.model;

import com.vladoose.todoapp.util.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.autoconfigure.web.WebProperties;

import java.time.LocalDate;
import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Temporal(TemporalType.DATE)
    @Column(name = "due_date")
    private LocalDate dueDate;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TaskStatus status;
}
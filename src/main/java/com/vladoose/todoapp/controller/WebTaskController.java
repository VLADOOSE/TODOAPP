package com.vladoose.todoapp.controller;

import com.vladoose.todoapp.dto.TaskDto;
import com.vladoose.todoapp.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;

@Controller
public class WebTaskController {

    private final TaskService taskService;

    public WebTaskController(TaskService taskService) {
        this.taskService = taskService;
    }
    @GetMapping("/tasks")
    public String tasksPage() {
        return "tasks";
    }

}



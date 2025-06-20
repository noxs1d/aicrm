package com.noxsid.nks.crmai.controllers;

import com.noxsid.nks.crmai.data.Status;
import com.noxsid.nks.crmai.data.Task;
import com.noxsid.nks.crmai.data.User;
import com.noxsid.nks.crmai.service.ProjectService;
import com.noxsid.nks.crmai.service.TaskService;
import com.noxsid.nks.crmai.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;
    private final ProjectService projectService;

    @GetMapping
    public String getTaskPage(Model model, HttpServletRequest request){

        model.addAttribute("activeMenu", "tasks");
        model.addAttribute("task", new Task());
        model.addAttribute("statuses", Status.values());
        model.addAttribute("projects", projectService.getAllProjects());
        model.addAttribute("tasks", taskService.getUsersTasks(userService.getUser(request)));
        return "task";
    }

    @PostMapping
    public String postTask(@ModelAttribute Task task, HttpServletRequest request){
        task.setUser(userService.getUser(request));
        task.setAssigned_date(LocalDateTime.now());
        taskService.saveTask(task);
        return "/task";
    }
}

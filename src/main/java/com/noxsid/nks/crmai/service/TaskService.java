package com.noxsid.nks.crmai.service;

import com.noxsid.nks.crmai.data.Status;
import com.noxsid.nks.crmai.data.Task;
import com.noxsid.nks.crmai.data.User;
import com.noxsid.nks.crmai.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    public List<Task> getUsersTasks(User user){
        return taskRepository.findAllByUser(user.getId());
    }

    public List<Task> getTasksByStatus(User user, Status status){
        return taskRepository.findByUserStatus(user.getId(),status);
    }
    public void saveTask(Task task){
        taskRepository.save(task);
    }
}

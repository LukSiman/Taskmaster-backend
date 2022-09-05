package net.lukassimanavicius.TaskmasterBE.services;

import net.lukassimanavicius.TaskmasterBE.entities.Task;
import net.lukassimanavicius.TaskmasterBE.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private final TaskRepository taskRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Return all tasks
     */
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    /**
     * Returns the task according to given ID
     */
    public Task getSingleTask(int id) {
        Task task = taskRepository.getReferenceById(id);
        return task;
    }
}

package net.lukassimanavicius.TaskmasterBE.controllers;

import net.lukassimanavicius.TaskmasterBE.dto.TaskDTO;
import net.lukassimanavicius.TaskmasterBE.entities.Task;
import net.lukassimanavicius.TaskmasterBE.services.TaskService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private final TaskService taskService;

    @Autowired
    private ModelMapper modelMapper;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Returns a list of all tasks
     */
    @GetMapping
    @ResponseBody
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        // get a list of task entities
        List<Task> tasks = taskService.getAllTasks();

        // convert entities to DTO
        List<TaskDTO> tasksDTO = new ArrayList<>();
        for (Task task : tasks) {
            tasksDTO.add(modelMapper.map(task, TaskDTO.class));
        }

        // return a list of DTO objects as a response entity
        return new ResponseEntity<>(tasksDTO, HttpStatus.OK);
    }


    /**
     * Returns a single task by ID
     */
    @GetMapping("{id}")
    @ResponseBody
    public ResponseEntity<TaskDTO> getTask(@PathVariable UUID id) {

        // get the task entity
        Task task = taskService.getSingleTask(id);

        // handle if task is null
        if (task == null) {
            throw new EntityNotFoundException();
        }

        // convert entity to DTO
        TaskDTO taskDTO = modelMapper.map(task, TaskDTO.class);

        // return the DTO as a response entity
        return new ResponseEntity<>(taskDTO, HttpStatus.OK);
    }

    /**
     * Saves the task to the database
     */
    @PostMapping("save")
    @ResponseBody
    public ResponseEntity<TaskDTO> addTask(@RequestBody TaskDTO taskDTO) {

        // convert DTO to entity
        Task taskRequest = modelMapper.map(taskDTO, Task.class);

        // save Task entity to DB
        Task task = taskService.saveTask(taskRequest);

        // convert entity to DTO
        TaskDTO taskResponse = modelMapper.map(task, TaskDTO.class);

        // return the DTO as a response entity
        return new ResponseEntity<>(taskResponse, HttpStatus.CREATED);
    }

    /**
     * Deletes the task from the database
     */
    @DeleteMapping("{id}")
    @ResponseBody
    public ResponseEntity deleteTask(@PathVariable UUID id) {

        // delete the task object from DB, throw exception if bad id
        String message = "";
        try {
            message = taskService.deleteTask(id);
        } catch (Exception exc) {
            throw new EntityNotFoundException();
        }

        // response message
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    /**
     * Handles a bad delete path
     */
    @DeleteMapping("")
    @ResponseBody
    public void badDeletePath() {
        throw new EntityNotFoundException();
    }

    /**
     * Updates the task with new data
     */
    @PutMapping("{id}")
    @ResponseBody
    public ResponseEntity<TaskDTO> updateTask(@RequestBody TaskDTO taskDTO) {

        // convert DTO to entity
        Task taskRequest = modelMapper.map(taskDTO, Task.class);

        // save Task entity to DB
        Task task = taskService.updateTask(taskRequest);

        // convert entity to DTO
        TaskDTO taskResponse = modelMapper.map(task, TaskDTO.class);

        // return the DTO as a response entity
        return new ResponseEntity<>(taskResponse, HttpStatus.OK);
    }

    /**
     * Handles exceptions for bad IDs
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class, EntityNotFoundException.class, MissingPathVariableException.class})
    private ResponseEntity handleBadID() {
        return ResponseEntity.badRequest().body("Bad id, please try again.");
    }
}

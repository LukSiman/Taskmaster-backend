package net.lukassimanavicius.TaskmasterBE.services;

import net.lukassimanavicius.TaskmasterBE.entities.Category;
import net.lukassimanavicius.TaskmasterBE.entities.Task;
import net.lukassimanavicius.TaskmasterBE.repositories.CategoryRepository;
import net.lukassimanavicius.TaskmasterBE.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
public class TaskService {

    @Autowired
    private final TaskRepository taskRepository;

    @Autowired
    private final CategoryRepository categoryRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, CategoryRepository categoryRepository) {
        this.taskRepository = taskRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * Return all tasks
     */
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    /**
     * Returns the task according to given UUID
     */
    public Task getSingleTask(UUID id) {
        Task task = taskRepository.findByTaskUUID(id);
        return task;
    }

    /**
     * Saves the task to the database
     */
    @Transactional
    public Task saveTask(Task taskToSave) {

        // create a UUID for the task
        taskToSave.setTaskUUID(UUID.randomUUID());

        // extract category if task entity has it
        if (taskToSave.getCategory() != null) {
            // extract the category name from the request
            String categoryName = taskToSave.getCategory().getCategoryName();

            // find the category by name
            Category category = categoryRepository.findByCategoryName(categoryName);

            // extract the ID from the category
            Integer categoryID = category.getCategoryID();

            // set the category ID to the task entity
            taskToSave.getCategory().setCategoryID(categoryID);
        } else {
            // set default category if none was given
            Category category = new Category();
            category.setCategoryName("Other");
            category.setCategoryID(12);
            taskToSave.setCategory(category);
        }

        Task task = taskRepository.save(taskToSave);

        return task;
    }
}

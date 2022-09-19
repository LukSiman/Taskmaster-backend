package net.lukassimanavicius.TaskmasterBE.services;

import net.lukassimanavicius.TaskmasterBE.entities.Category;
import net.lukassimanavicius.TaskmasterBE.entities.Task;
import net.lukassimanavicius.TaskmasterBE.repositories.CategoryRepository;
import net.lukassimanavicius.TaskmasterBE.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
public class TaskService {

    static final int DEFAULT_CATEGORY_ID = 12;
    static final String DEFAULT_CATEGORY_NAME = "Other";

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
     * Saves the task to the database and returns it
     */
    @Transactional
    public Task saveTask(Task taskToSave) {

        // create a UUID for the task
        taskToSave.setTaskUUID(UUID.randomUUID());

        // set current date as default if no date is given
        if (taskToSave.getTaskDate() == null) {
            taskToSave.setTaskDate(LocalDate.now());
        }

        // set the category
        Category category = categoryProcessing(taskToSave);
        taskToSave.setCategory(category);

        Task task = taskRepository.save(taskToSave);

        return task;
    }

    /**
     * Deletes the task from the database and returns the name
     */
    @Transactional
    public String deleteTask(UUID id) {
        // get the task name
        String taskName = taskRepository.findByTaskUUID(id).getTaskName();

        // delete the task from DB
        taskRepository.deleteByTaskUUID(id);

        return taskName + " has been deleted successfully!";
    }

    /**
     * Updates the task in DB and returns it
     */
    @Transactional
    public Task updateTask(Task taskUpdateDetails) {
        // Get the task object that will be updated
        Task taskToUpdate = taskRepository.findByTaskUUID(taskUpdateDetails.getTaskUUID());

        // Update fields with new data
        taskToUpdate.setTaskName(taskUpdateDetails.getTaskName());
        taskToUpdate.setTaskOrder(taskUpdateDetails.getTaskOrder());
        taskToUpdate.setTaskNote(taskUpdateDetails.getTaskNote());
        taskToUpdate.setTaskStatus(taskUpdateDetails.getTaskStatus());
        taskToUpdate.setTaskStartTime(taskUpdateDetails.getTaskStartTime());
        taskToUpdate.setTaskEndTime(taskUpdateDetails.getTaskEndTime());
        taskToUpdate.setTaskDate(taskUpdateDetails.getTaskDate());
        Category category = categoryProcessing(taskUpdateDetails);
        taskToUpdate.setCategory(category);

        return taskRepository.save(taskToUpdate);
    }

    /**
     * Handle categories when saving and updating
     */
    private Category categoryProcessing(Task task){
        Category category = new Category();
        // extract category if task entity has it
        if (task.getCategory() != null) {
            // extract the category name from the request
            String categoryName = task.getCategory().getCategoryName();

            // find the category by name
            category = categoryRepository.findByCategoryName(categoryName);
        } else {
            // set default category if none was given
            category.setCategoryName(DEFAULT_CATEGORY_NAME);
            category.setCategoryID(DEFAULT_CATEGORY_ID);
        }
        return category;
    }
}

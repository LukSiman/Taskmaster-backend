package net.lukassimanavicius.TaskmasterBE.services;

import net.lukassimanavicius.TaskmasterBE.entities.Category;
import net.lukassimanavicius.TaskmasterBE.entities.Task;
import net.lukassimanavicius.TaskmasterBE.exceptions.BadTimeException;
import net.lukassimanavicius.TaskmasterBE.repositories.CategoryRepository;
import net.lukassimanavicius.TaskmasterBE.repositories.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
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
     * Returns tasks for the provided date
     */
    public List<Task> getDateTasks(LocalDate date) {
        return taskRepository.findByTaskDate(date);
    }

    /**
     * Returns the task according to given UUID
     */
    public Task getSingleTask(UUID id) {
        return taskRepository.findByTaskUUID(id);
    }

    /**
     * Saves the task to the database and returns it
     */
    @Transactional
    public Task saveTask(Task taskToSave) {

        // create a UUID for the task
        taskToSave.setTaskUUID(UUID.randomUUID());

        // handles correctness for start and end times
        LocalTime startTime = taskToSave.getTaskStartTime();
        LocalTime endTime = taskToSave.getTaskEndTime();
        timeProcessor(startTime, endTime);

        // set current date as default if no date is given
        if (taskToSave.getTaskDate() == null) {
            taskToSave.setTaskDate(LocalDate.now());
        }

        // set the category
        Category category = categoryProcessing(taskToSave);
        taskToSave.setCategory(category);

        return taskRepository.save(taskToSave);
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

        // handles correctness for start and end times
        LocalTime startTime = taskUpdateDetails.getTaskStartTime();
        LocalTime endTime = taskUpdateDetails.getTaskEndTime();
        timeProcessor(startTime, endTime);

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
    private Category categoryProcessing(Task task) {
        Category category = new Category();

        // extract category if task entity has it
        if (task.getCategory() != null) {
            // extract the category name from the request
            String categoryName = task.getCategory().getCategoryName();

            // find the category by name
            category = categoryRepository.findByCategoryName(categoryName);
        }

        if (task.getCategory() == null || category == null) {
            // set default category if none was given or invalid category provided
            category = new Category();
            category.setCategoryName(DEFAULT_CATEGORY_NAME);
            category.setCategoryID(DEFAULT_CATEGORY_ID);
        }
        return category;
    }

    /**
     * Checks if start time and end time is set up correctly
     */
    private void timeProcessor(LocalTime startTime, LocalTime endTime) {
        // throw exception if end time is before start time
        if (startTime != null && endTime != null) {
            if (!startTime.isBefore(endTime)) {
                throw new BadTimeException("End time is before the start time!");
            }
            // throw exception if only end time has been entered
        } else if (startTime == null && endTime != null) {
            throw new BadTimeException("You're missing start time!");
        }
    }
}

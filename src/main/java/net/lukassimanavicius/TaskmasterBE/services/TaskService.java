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
import java.time.LocalDateTime;
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
        timeProcessor(taskToSave);

        // set the order of the task
        //TODO: FINISH somehow find what should the correct order be
        //TODO: if no time, goes last
        //TODO: if time then in correct spot
//        handleCorrectOrder(taskToSave);

        // By default status is 0 (not completed)
        taskToSave.setTaskStatus(0);

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
//        taskToUpdate.setTaskOrder(taskUpdateDetails.getTaskOrder());
        taskToUpdate.setTaskNote(taskUpdateDetails.getTaskNote());
        taskToUpdate.setTaskStatus(taskUpdateDetails.getTaskStatus());

        // handles correctness for start and end times
        timeProcessor(taskUpdateDetails);

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
    private void timeProcessor(Task taskToSave) {
        LocalDateTime startTime = taskToSave.getTaskStartTime();
        LocalDateTime endTime = taskToSave.getTaskEndTime();

        // throw exception if end time is before start time
        if (startTime != null && endTime != null) {
            if (!startTime.isBefore(endTime)) {
                throw new BadTimeException("End time is before the start time!");
            }
            // throw exception if only end time has been entered
        } else if (startTime == null && endTime != null) {
            throw new BadTimeException("You're missing start time!");
            // if both are null set as 00:00:00 time of the date
        } else if (startTime == null && endTime == null) {
            startTime = LocalDateTime.of(taskToSave.getTaskDate(), LocalTime.parse("00:00:00"));
            endTime = LocalDateTime.of(taskToSave.getTaskDate(), LocalTime.parse("00:00:00"));
            taskToSave.setTaskStartTime(startTime);
            taskToSave.setTaskEndTime(endTime);
        }
    }

    /**
     * Sets the correct task order
     */
    private void handleCorrectOrder(Task task) {
        if (task.getTaskStartTime() == null) {
            // gets the amount of tasks for the date
            int sizeOfDay = getDateTasks(task.getTaskDate()).size();

            // increments by 1 and sets it as the order
            sizeOfDay++;
//            task.setTaskOrder(sizeOfDay);
        }
    }
}


//    private void handleCorrectOrder(Task task) {
//
//        if (task.getTaskStartTime() == null) {
//            // gets the amount of tasks for the date
//            int sizeOfDay = getDateTasks(task.getTaskDate()).size();
//
//            // increments by 1 and sets it as the order
//            sizeOfDay++;
//            task.setTaskOrder(sizeOfDay);
//        } else {
//            // get the task list
//            List<Task> taskList = getDateTasks(task.getTaskDate());
//
//            // set order to 1 if no tasks were in the day
//            if (taskList.size() == 0) {
//                task.setTaskOrder(1);
//                return;
//            }
//
//            // get the start time of the task
//            LocalDateTime startTime = task.getTaskStartTime();
//            boolean orderSet = false;
//
//            // check times for every task and decide the order
//            for (Task taskToCheck : taskList) {
//
//                // check if new order has been set and update the remaining orders
//                if (orderSet) {
//                    int oldOrder = taskToCheck.getTaskOrder();
//                    taskToCheck.setTaskOrder(++oldOrder);
//                    updateTask(taskToCheck);
//                }
//
//                // get the start time of the task from the list
//                LocalDateTime startTimeToCheck = taskToCheck.getTaskStartTime();
//
//                // check if task if before the checked task and set the new order
//                if (startTime.isBefore(startTimeToCheck)) {
//                    int oldOrder = taskToCheck.getTaskOrder();
//                    task.setTaskOrder(oldOrder);
//                    taskToCheck.setTaskOrder(++oldOrder);
//                    updateTask(taskToCheck);
//                    orderSet = true;
//
//                    //TODO: Split into more methods, getting too complex
//                    //TODO: After changing the order all tasks need to be looped again
//                    //TODO: Idea: Get rid of ordering in the database and process the list on the backend? Simple sort?
//                }
//            }
//
//            if (task.getTaskOrder() == null) {
//                int sizeOfDay = getDateTasks(task.getTaskDate()).size();
//
//                // increments by 1 and sets it as the order
//                sizeOfDay++;
//                task.setTaskOrder(sizeOfDay);
//            }
//        }
//    }
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
import java.util.Comparator;
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
        List<Task> returnList = taskRepository.findAll();

        returnList = handleCorrectOrder(returnList);

        return returnList;
//        return taskRepository.findAll();
    }

    /**
     * Returns tasks for the provided date
     */
    public List<Task> getDateTasks(LocalDate date) {
        List<Task> returnList = taskRepository.findByTaskDate(date);

        returnList = handleCorrectOrder(returnList);

        return returnList;
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

        // By default, status is 0 (not completed)
        taskToSave.setTaskStatus(0);

        // check if note is empty
        noteProcessing(taskToSave);

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
        taskToUpdate.setTaskStatus(taskUpdateDetails.getTaskStatus());

        // check if note is empty
        noteProcessing(taskUpdateDetails);
        taskToUpdate.setTaskNote(taskUpdateDetails.getTaskNote());

        // handles correctness for start and end times
        timeProcessor(taskUpdateDetails);
        // sets the times
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
     * Checks if note is of zero length
     */
    private void noteProcessing(Task taskToSave) {
        String note = taskToSave.getTaskNote();
        if (note.trim().length() == 0) {
            taskToSave.setTaskNote(null);
        }
    }

    /**
     * Sorts a list of tasks by their start times in ascending order.
     * Tasks with a start time of 00:00 are moved to the end of the list.
     */
    private List<Task> handleCorrectOrder(List<Task> listToSort) {

        // Create a comparator that compares the start times
        Comparator<Task> startTimeComparator = new Comparator<Task>() {
            @Override
            public int compare(Task task1, Task task2) {
                // Local variables for start times
                LocalDateTime startTime1 = task1.getTaskStartTime();
                LocalDateTime startTime2 = task2.getTaskStartTime();

                // Check if start times are at 00:00 and handle accordingly
                boolean isStartTime1Midnight = startTime1.getHour() == 0 && startTime1.getMinute() == 0;
                boolean isStartTime2Midnight = startTime2.getHour() == 0 && startTime2.getMinute() == 0;

                if (isStartTime1Midnight && isStartTime2Midnight) {
                    return 0;
                } else if (isStartTime1Midnight) {
                    // Move task1 to the end
                    return 1;
                } else if (isStartTime2Midnight) {
                    // Move task2 to the end
                    return -1;
                }

                // Check which task starts before
                if (startTime1.isBefore(startTime2)) {
                    // Move task1 to the front
                    return -1;
                } else if (startTime1.isAfter(startTime2)) {
                    // Move task1 to the back
                    return 1;
                } else {
                    return 0;
                }
            }
        };

        // Sort the list
        listToSort.sort(startTimeComparator);

        return listToSort;
    }
}
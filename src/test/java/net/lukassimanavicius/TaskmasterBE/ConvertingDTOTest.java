package net.lukassimanavicius.TaskmasterBE;

import net.lukassimanavicius.TaskmasterBE.dto.CategoryDTO;
import net.lukassimanavicius.TaskmasterBE.dto.TaskDTO;
import net.lukassimanavicius.TaskmasterBE.entities.Category;
import net.lukassimanavicius.TaskmasterBE.entities.Task;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConvertingDTOTest {

    private final ModelMapper modelMapper = new ModelMapper();

    @Test
    public void convertCategoryToDTOTest() {
        Category category = new Category();
        String name = "TestTest";
        category.setCategoryName(name);

        CategoryDTO categoryDTO = modelMapper.map(category, CategoryDTO.class);
        assertEquals(name, category.getCategoryName());
        assertEquals(category.getCategoryName(), categoryDTO.getCategoryName());
    }

    @Test
    public void convertDTOToCategoryTest() {
        Category category = new Category();
        String name = "TestTest";
        category.setCategoryName(name);

        CategoryDTO categoryDTO = modelMapper.map(category, CategoryDTO.class);

        assertEquals(name, categoryDTO.getCategoryName());
        assertEquals(categoryDTO.getCategoryName(), category.getCategoryName());
    }

    @Test
    public void convertTaskToDTOTest() {
        Task task = new Task();
        String name = "TestTest";
        task.setTaskName(name);

        TaskDTO taskDTO = modelMapper.map(task, TaskDTO.class);
        assertEquals(name, task.getTaskName());
        assertEquals(task.getTaskName(), taskDTO.getTaskName());
    }

    @Test
    public void convertDTOToTaskTest() {
        Task task = new Task();
        String name = "TestTest";
        task.setTaskName(name);

        TaskDTO taskDTO = modelMapper.map(task, TaskDTO.class);

        assertEquals(name, taskDTO.getTaskName());
        assertEquals(taskDTO.getTaskName(), task.getTaskName());
    }

    @Test
    public void setTaskDTOTest() {
        Task task = new Task();

        Integer id = 99;
        task.setTaskID(id);

        String name = "Test";
        task.setTaskName(name);

        LocalDate date = LocalDate.parse("2022-09-04");
        task.setTaskDate(date);

        String note = "Big Test";
        task.setTaskNote(note);

        Integer order = 5;
        task.setTaskOrder(order);

        Integer status = 1;
        task.setTaskStatus(status);

        LocalTime startTime = LocalTime.parse("18:20:00");
        task.setTaskStartTime(startTime);

        LocalTime endTime = LocalTime.parse("19:20:00");
        task.setTaskEndTime(endTime);

        TaskDTO taskDTO = modelMapper.map(task, TaskDTO.class);

        assertEquals(id, task.getTaskID());
        assertEquals(name, task.getTaskName());
        assertEquals(date, task.getTaskDate());
        assertEquals(note, task.getTaskNote());
        assertEquals(order, task.getTaskOrder());
        assertEquals(status, task.getTaskStatus());
        assertEquals(startTime, task.getTaskStartTime());
        assertEquals(endTime, task.getTaskEndTime());
        assertEquals(taskDTO.getTaskName(), task.getTaskName());
        assertEquals(taskDTO.getTaskDate(), task.getTaskDate());
        assertEquals(taskDTO.getTaskNote(), task.getTaskNote());
        assertEquals(taskDTO.getTaskOrder(), task.getTaskOrder());
        assertEquals(taskDTO.getTaskStatus(), task.getTaskStatus());
        assertEquals(taskDTO.getTaskStartTime(), task.getTaskStartTime());
        assertEquals(taskDTO.getTaskEndTime(), task.getTaskEndTime());
    }
}

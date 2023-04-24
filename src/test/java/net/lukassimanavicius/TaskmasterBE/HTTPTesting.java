package net.lukassimanavicius.TaskmasterBE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.JsonPath;
import net.lukassimanavicius.TaskmasterBE.dto.TaskDTO;
import net.lukassimanavicius.TaskmasterBE.services.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class HTTPTesting {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryService categoryService;

    @Test
    public void getSingleCategoryTest() throws Exception {
        ResultActions response = mockMvc.perform(get("/categories/{id}", 1));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("categoryName", is("Work")));
    }

    @Test
    public void getSingleBadCategoryTest() throws Exception {
        ResultActions response = mockMvc.perform(get("/categories/{id}", 99));

        response.andExpect(status().isBadRequest())
                .andDo(print()).andExpect(content().string("Bad id, please try again!"));
    }

    @Test
    public void getSingleEmptyCategoryTest() throws Exception {
        ResultActions response = mockMvc.perform(get("/categories/{id}", " "));

        response.andExpect(status().isBadRequest())
                .andDo(print()).andExpect(content().string("Bad id, please try again!"));
    }

    @Test
    public void getSingleNotNumberCategoryTest() throws Exception {
        ResultActions response = mockMvc.perform(get("/categories/{id}", "c"));

        response.andExpect(status().isBadRequest())
                .andDo(print()).andExpect(content().string("Bad id, please try again!"));
    }

    @Test
    public void getSeveralSingleCategoryTest() throws Exception {
        ResultActions response1 = mockMvc.perform(get("/categories/{id}", 2));
        ResultActions response2 = mockMvc.perform(get("/categories/{id}", 4));
        ResultActions response3 = mockMvc.perform(get("/categories/{id}", 6));
        ResultActions response4 = mockMvc.perform(get("/categories/{id}", 9));
        ResultActions response5 = mockMvc.perform(get("/categories/{id}", 12));


        response1.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("categoryName", is("Entertainment")));

        response2.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("categoryName", is("Fitness")));

        response3.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("categoryName", is("Medical")));

        response4.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("categoryName", is("Household")));

        response5.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("categoryName", is("Other")));
    }

    @Test
    public void getAllCategoryTest() throws Exception {
        ResultActions response = mockMvc.perform(get("/categories"));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(12)));
    }


    @Test
    public void getSingleTaskTest() throws Exception {
        ResultActions response = mockMvc.perform(get("/tasks/{id}", "cee39fa5-3e03-11ed-85b7-fcaa14e3878f")); //1

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("taskName", is("Wash up")))
                .andExpect(jsonPath("taskOrder", is(1)))
                .andExpect(jsonPath("taskNote", is("")))
                .andExpect(jsonPath("taskStatus", is(1)))
                .andExpect(jsonPath("taskStartTime", is("08:00:00")))
                .andExpect(jsonPath("taskEndTime", is("08:15:00")))
                .andExpect(jsonPath("taskDate", is("2022-08-28")))
                .andExpect(jsonPath("categoryName", is("Beauty")));
    }

    @Test
    public void getSingleBadTaskTest() throws Exception {
        ResultActions response = mockMvc.perform(get("/tasks/{id}", 99));

        response.andExpect(status().isBadRequest())
                .andDo(print()).andExpect(content().string("Bad id, please try again!"));
    }

    @Test
    public void getSingleRandomIDTaskTest() throws Exception {
        UUID randomID = UUID.randomUUID();
        System.out.println(randomID);
        ResultActions response = mockMvc.perform(get("/tasks/{id}", randomID));

        response.andExpect(status().isBadRequest())
                .andDo(print()).andExpect(content().string("Bad id, please try again!"));
    }

    @Test
    public void getSingleEmptyTaskTest() throws Exception {
        ResultActions response = mockMvc.perform(get("/tasks/{id}", " "));

        response.andExpect(status().isBadRequest())
                .andDo(print()).andExpect(content().string("Bad id, please try again!"));
    }

    @Test
    public void getSingleNotNumberTaskTest() throws Exception {
        ResultActions response = mockMvc.perform(get("/tasks/{id}", "c"));

        response.andExpect(status().isBadRequest())
                .andDo(print()).andExpect(content().string("Bad id, please try again!"));
    }

    @Test
    public void getSeveralSingleTaskTest() throws Exception {
        ResultActions response1 = mockMvc.perform(get("/tasks/{id}", "cee50563-3e03-11ed-85b7-fcaa14e3878f")); //2
        ResultActions response2 = mockMvc.perform(get("/tasks/{id}", "ceef06a6-3e03-11ed-85b7-fcaa14e3878f")); //8
        ResultActions response3 = mockMvc.perform(get("/tasks/{id}", "cef8deed-3e03-11ed-85b7-fcaa14e3878f")); //14


        response1.andExpect(status().isOk())
                .andExpect(jsonPath("taskName", is("Breakfast")))
                .andExpect(jsonPath("taskOrder", is(2)))
                .andExpect(jsonPath("taskNote", is("Overnight oats")))
                .andExpect(jsonPath("taskStatus", is(1)))
                .andExpect(jsonPath("taskStartTime", is("08:15:00")))
                .andExpect(jsonPath("taskEndTime", is("08:30:00")))
                .andExpect(jsonPath("taskDate", is("2022-08-28")))
                .andExpect(jsonPath("categoryName", is("Food")));

        response2.andExpect(status().isOk())
                .andExpect(jsonPath("taskName", is("Learning")))
                .andExpect(jsonPath("taskOrder", is(8)))
                .andExpect(jsonPath("taskNote", is("Algos chapter 11")))
                .andExpect(jsonPath("taskStatus", is(0)))
                .andExpect(jsonPath("taskStartTime", is("20:30:00")))
                .andExpect(jsonPath("taskEndTime", is("22:00:00")))
                .andExpect(jsonPath("taskDate", is("2022-08-28")))
                .andExpect(jsonPath("categoryName", is("Education")));

        response3.andExpect(status().isOk())
                .andExpect(jsonPath("taskName", is("Task3")))
                .andExpect(jsonPath("taskOrder", is(3)))
                .andExpect(jsonPath("taskNote", is("Random note")))
                .andExpect(jsonPath("taskStatus", is(0)))
                .andExpect(jsonPath("taskStartTime").doesNotExist())
                .andExpect(jsonPath("taskEndTime").doesNotExist())
                .andExpect(jsonPath("taskDate", is("2022-08-29")))
                .andExpect(jsonPath("categoryName", is("Other")));
    }

    @Test
    public void getAllTasksTest() throws Exception {
        ResultActions response = mockMvc.perform(get("/tasks"));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(26)));
    }

    @Test
    public void createNewTaskMinimalTest() throws Exception {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTaskName("Control the world");
        taskDTO.setTaskOrder(1);
        taskDTO.setTaskStatus(0);
        taskDTO.setTaskDate(LocalDate.parse("2022-12-25"));

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String jsonTask = mapper.writeValueAsString(taskDTO);

        ResultActions response = mockMvc.perform(post("/tasks/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTask));

        response.andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("taskName", is("Control the world")))
                .andExpect(jsonPath("taskOrder", is(1)))
                .andExpect(jsonPath("taskNote").doesNotExist())
                .andExpect(jsonPath("taskStatus", is(0)))
                .andExpect(jsonPath("taskStartTime").doesNotExist())
                .andExpect(jsonPath("taskEndTime").doesNotExist())
                .andExpect(jsonPath("taskDate", is("2022-12-25")))
                .andExpect(jsonPath("categoryName", is("Other")));
    }

    @Test
    public void createNewTaskFullTest() throws Exception {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTaskName("Control the world");
        taskDTO.setTaskOrder(1);
        taskDTO.setTaskNote("Testing testing 123!");
        taskDTO.setTaskStatus(1);
        taskDTO.setTaskStartTime(LocalDateTime.of(LocalDate.parse("2022-12-25"), LocalTime.parse("15:00:00")));
        taskDTO.setTaskEndTime(LocalDateTime.of(LocalDate.parse("2022-12-25"), LocalTime.parse("19:35:00")));
        taskDTO.setTaskDate(LocalDate.parse("2022-12-25"));
        taskDTO.setCategoryName("Medical");

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String jsonTask = mapper.writeValueAsString(taskDTO);

        ResultActions response = mockMvc.perform(post("/tasks/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTask));

        response.andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("taskName", is("Control the world")))
                .andExpect(jsonPath("taskOrder", is(1)))
                .andExpect(jsonPath("taskNote", is("Testing testing 123!")))
                .andExpect(jsonPath("taskStatus", is(1)))
                .andExpect(jsonPath("taskStartTime", is("2022-12-25 15:00:00")))
                .andExpect(jsonPath("taskEndTime", is("2022-12-25 19:35:00")))
                .andExpect(jsonPath("taskDate", is("2022-12-25")))
                .andExpect(jsonPath("categoryName", is("Medical")));
    }

    @Test
    public void deleteTaskGood() throws Exception {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTaskName("Control the world");
        taskDTO.setTaskOrder(1);
        taskDTO.setTaskStatus(1);
        taskDTO.setTaskDate(LocalDate.parse("2022-12-25"));

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String jsonTask = mapper.writeValueAsString(taskDTO);

        MvcResult response = mockMvc.perform(post("/tasks/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTask)).andReturn();

        String uuid = JsonPath.read(response.getResponse().getContentAsString(), "taskUUID");

        ResultActions deleteResponse = mockMvc.perform(delete("/tasks/" + uuid));

        deleteResponse.andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string(taskDTO.getTaskName() + " has been deleted successfully!"));
    }

    @Test
    public void deleteTaskBadID() throws Exception {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTaskName("Control the world");
        taskDTO.setTaskOrder(1);
        taskDTO.setTaskDate(LocalDate.parse("2022-12-25"));

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String jsonTask = mapper.writeValueAsString(taskDTO);

        mockMvc.perform(post("/tasks/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTask));

        ResultActions response = mockMvc.perform(delete("/tasks/"));

        response.andExpect(status().isBadRequest())
                .andDo(print()).andExpect(content().string("Bad id, please try again!"));

        response = mockMvc.perform(delete("/tasks/" + ""));

        response.andExpect(status().isBadRequest())
                .andDo(print()).andExpect(content().string("Bad id, please try again!"));

        response = mockMvc.perform(delete("/tasks/" + " "));

        response.andExpect(status().isBadRequest())
                .andDo(print()).andExpect(content().string("Bad id, please try again!"));

        response = mockMvc.perform(delete("/tasks/" + 9));

        response.andExpect(status().isBadRequest())
                .andDo(print()).andExpect(content().string("Bad id, please try again!"));

        response = mockMvc.perform(delete("/tasks/" + "9"));

        response.andExpect(status().isBadRequest())
                .andDo(print()).andExpect(content().string("Bad id, please try again!"));

        response = mockMvc.perform(delete("/tasks/" + "c"));

        response.andExpect(status().isBadRequest())
                .andDo(print()).andExpect(content().string("Bad id, please try again!"));

        response = mockMvc.perform(delete("/tasks/" + "70305a91-33b6-11ed-ac36-fcaa14e3878f"));

        response.andExpect(status().isBadRequest())
                .andDo(print()).andExpect(content().string("Bad id, please try again!"));
    }

    @Test
    public void partialUpdateTaskTest() throws Exception {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTaskName("Control the world");
        taskDTO.setTaskOrder(1);
        taskDTO.setTaskNote("Testing testing 123!");
        taskDTO.setTaskStatus(1);
        taskDTO.setTaskStartTime(LocalDateTime.of(LocalDate.parse("2022-12-25"), LocalTime.parse("15:00:00")));
        taskDTO.setTaskEndTime(LocalDateTime.of(LocalDate.parse("2022-12-25"), LocalTime.parse("19:35:00")));
        taskDTO.setTaskDate(LocalDate.parse("2022-12-25"));
        taskDTO.setCategoryName("Medical");

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String jsonTask = mapper.writeValueAsString(taskDTO);

        ResultActions createResponse = mockMvc.perform(post("/tasks/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTask));

        String originalUUID = JsonPath.read(createResponse.andReturn().getResponse().getContentAsString(), "taskUUID");
        taskDTO.setTaskUUID(UUID.fromString(originalUUID));

        createResponse.andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("taskName", is("Control the world")))
                .andExpect(jsonPath("taskOrder", is(1)))
                .andExpect(jsonPath("taskNote", is("Testing testing 123!")))
                .andExpect(jsonPath("taskStatus", is(1)))
                .andExpect(jsonPath("taskStartTime", is("2022-12-25 15:00:00")))
                .andExpect(jsonPath("taskEndTime", is("2022-12-25 19:35:00")))
                .andExpect(jsonPath("taskDate", is("2022-12-25")))
                .andExpect(jsonPath("categoryName", is("Medical")));

        taskDTO.setTaskName("Update the world");
        taskDTO.setTaskOrder(2);
        taskDTO.setTaskNote("This must be changed");
        taskDTO.setTaskStatus(1);
        taskDTO.setTaskStartTime(LocalDateTime.of(LocalDate.parse("2022-12-25"), LocalTime.parse("15:00:00")));
        taskDTO.setTaskEndTime(LocalDateTime.of(LocalDate.parse("2022-12-25"), LocalTime.parse("19:35:00")));
        taskDTO.setTaskDate(LocalDate.parse("2022-12-25"));
        taskDTO.setCategoryName("Medical");

        String updateJsonTask = mapper.writeValueAsString(taskDTO);

        ResultActions updateResponse = mockMvc.perform(put("/tasks/" + originalUUID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJsonTask));

        updateResponse.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("taskUUID", is(originalUUID)))
                .andExpect(jsonPath("taskName", is("Update the world")))
                .andExpect(jsonPath("taskOrder", is(2)))
                .andExpect(jsonPath("taskNote", is("This must be changed")))
                .andExpect(jsonPath("taskStatus", is(1)))
                .andExpect(jsonPath("taskStartTime", is("2022-12-25 15:00:00")))
                .andExpect(jsonPath("taskEndTime", is("2022-12-25 19:35:00")))
                .andExpect(jsonPath("taskDate", is("2022-12-25")))
                .andExpect(jsonPath("categoryName", is("Medical")));

        ResultActions deleteResponse = mockMvc.perform(delete("/tasks/" + originalUUID));
        deleteResponse.andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string(taskDTO.getTaskName() + " has been deleted successfully!"));
    }

    @Test
    public void fullUpdateTaskTest() throws Exception {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTaskName("Control the world");
        taskDTO.setTaskOrder(1);
        taskDTO.setTaskNote("Testing testing 123!");
        taskDTO.setTaskStatus(1);
        taskDTO.setTaskStartTime(LocalDateTime.of(LocalDate.parse("2022-12-25"), LocalTime.parse("15:00:00")));
        taskDTO.setTaskEndTime(LocalDateTime.of(LocalDate.parse("2022-12-25"), LocalTime.parse("19:35:00")));
        taskDTO.setTaskDate(LocalDate.parse("2022-12-25"));
        taskDTO.setCategoryName("Medical");

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String jsonTask = mapper.writeValueAsString(taskDTO);

        ResultActions createResponse = mockMvc.perform(post("/tasks/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTask));

        String originalUUID = JsonPath.read(createResponse.andReturn().getResponse().getContentAsString(), "taskUUID");
        taskDTO.setTaskUUID(UUID.fromString(originalUUID));

        createResponse.andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("taskName", is("Control the world")))
                .andExpect(jsonPath("taskOrder", is(1)))
                .andExpect(jsonPath("taskNote", is("Testing testing 123!")))
                .andExpect(jsonPath("taskStatus", is(1)))
                .andExpect(jsonPath("taskStartTime", is("2022-12-25 15:00:00")))
                .andExpect(jsonPath("taskEndTime", is("2022-12-25 19:35:00")))
                .andExpect(jsonPath("taskDate", is("2022-12-25")))
                .andExpect(jsonPath("categoryName", is("Medical")));

        taskDTO.setTaskName("Update the world");
        taskDTO.setTaskOrder(2);
        taskDTO.setTaskNote("This must be changed");
        taskDTO.setTaskStatus(0);
        taskDTO.setTaskStartTime(LocalDateTime.of(LocalDate.parse("2009-01-02"), LocalTime.parse("21:00:00")));
        taskDTO.setTaskEndTime(LocalDateTime.of(LocalDate.parse("2009-01-02"), LocalTime.parse("22:45:00")));
        taskDTO.setTaskDate(LocalDate.parse("2009-01-02"));
        taskDTO.setCategoryName("Entertainment");

        String updateJsonTask = mapper.writeValueAsString(taskDTO);

        ResultActions updateResponse = mockMvc.perform(put("/tasks/" + taskDTO.getTaskUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJsonTask));

        updateResponse.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("taskUUID", is(originalUUID)))
                .andExpect(jsonPath("taskName", is("Update the world")))
                .andExpect(jsonPath("taskOrder", is(2)))
                .andExpect(jsonPath("taskNote", is("This must be changed")))
                .andExpect(jsonPath("taskStatus", is(0)))
                .andExpect(jsonPath("taskStartTime", is("2009-01-02 21:00:00")))
                .andExpect(jsonPath("taskEndTime", is("2009-01-02 22:45:00")))
                .andExpect(jsonPath("taskDate", is("2009-01-02")))
                .andExpect(jsonPath("categoryName", is("Entertainment")));

        ResultActions deleteResponse = mockMvc.perform(delete("/tasks/" + taskDTO.getTaskUUID()));
        deleteResponse.andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string(taskDTO.getTaskName() + " has been deleted successfully!"));
    }

    @Test
    public void badDataCreationTest() throws Exception {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTaskName("g6ERzuQC9uSJ2K5gjgOxE8JwpfdxHEMgObOH0K9pXRx9EFZQe9MV");
        taskDTO.setTaskOrder(1);
        taskDTO.setTaskNote("Testing testing 123!");
        taskDTO.setTaskStatus(1);
        taskDTO.setTaskStartTime(LocalDateTime.of(LocalDate.parse("2022-12-25"), LocalTime.parse("15:00:00")));
        taskDTO.setTaskEndTime(LocalDateTime.of(LocalDate.parse("2022-12-25"), LocalTime.parse("19:35:00")));
        taskDTO.setTaskDate(LocalDate.parse("2022-12-25"));
        taskDTO.setCategoryName("Test");

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String jsonTask = mapper.writeValueAsString(taskDTO);

        ResultActions response = mockMvc.perform(post("/tasks/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTask));

        response.andExpect(status().isBadRequest())
                .andDo(print()).andExpect(content().string("Name must be between 2 and 50 characters!"));

        taskDTO.setTaskName("");
        jsonTask = mapper.writeValueAsString(taskDTO);

        response = mockMvc.perform(post("/tasks/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTask));

        response.andExpect(status().isBadRequest())
                .andDo(print()).andExpect(content().string("Name must be between 2 and 50 characters!"));

        taskDTO.setTaskName(null);
        jsonTask = mapper.writeValueAsString(taskDTO);

        response = mockMvc.perform(post("/tasks/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTask));

        response.andExpect(status().isBadRequest())
                .andDo(print()).andExpect(content().string("Name must be between 2 and 50 characters!"));

        taskDTO.setTaskName(" ");
        jsonTask = mapper.writeValueAsString(taskDTO);

        response = mockMvc.perform(post("/tasks/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTask));

        response.andExpect(status().isBadRequest())
                .andDo(print()).andExpect(content().string("Name must be between 2 and 50 characters!"));

        taskDTO.setTaskName("A");
        jsonTask = mapper.writeValueAsString(taskDTO);

        response = mockMvc.perform(post("/tasks/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTask));

        response.andExpect(status().isBadRequest())
                .andDo(print()).andExpect(content().string("Name must be between 2 and 50 characters!"));

        taskDTO.setTaskName("Control the world");
        taskDTO.setTaskOrder(0);
        jsonTask = mapper.writeValueAsString(taskDTO);

        response = mockMvc.perform(post("/tasks/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTask));

        response.andExpect(status().isBadRequest())
                .andDo(print()).andExpect(content().string("Invalid order value!"));

        taskDTO.setTaskOrder(-1);
        jsonTask = mapper.writeValueAsString(taskDTO);

        response = mockMvc.perform(post("/tasks/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTask));

        response.andExpect(status().isBadRequest())
                .andDo(print()).andExpect(content().string("Invalid order value!"));

        taskDTO.setTaskOrder(null);
        jsonTask = mapper.writeValueAsString(taskDTO);

        response = mockMvc.perform(post("/tasks/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTask));

        response.andExpect(status().isBadRequest())
                .andDo(print()).andExpect(content().string("Invalid order value!"));

        taskDTO.setTaskOrder(1);
        taskDTO.setTaskNote("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec qua");
        jsonTask = mapper.writeValueAsString(taskDTO);

        response = mockMvc.perform(post("/tasks/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTask));

        response.andExpect(status().isBadRequest())
                .andDo(print()).andExpect(content().string("Note cannot be longer than 200 characters!"));

        taskDTO.setTaskNote("Testing testing 123!");
        taskDTO.setTaskStatus(null);
        jsonTask = mapper.writeValueAsString(taskDTO);

        response = mockMvc.perform(post("/tasks/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTask));

        response.andExpect(status().isBadRequest())
                .andDo(print()).andExpect(content().string("Status cannot be null!"));

        taskDTO.setTaskStatus(-1);
        jsonTask = mapper.writeValueAsString(taskDTO);

        response = mockMvc.perform(post("/tasks/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTask));

        response.andExpect(status().isBadRequest())
                .andDo(print()).andExpect(content().string("Status has to be 0 or 1!"));

        taskDTO.setTaskStatus(-99);
        jsonTask = mapper.writeValueAsString(taskDTO);

        response = mockMvc.perform(post("/tasks/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTask));

        response.andExpect(status().isBadRequest())
                .andDo(print()).andExpect(content().string("Status has to be 0 or 1!"));

        taskDTO.setTaskStatus(2);
        jsonTask = mapper.writeValueAsString(taskDTO);

        response = mockMvc.perform(post("/tasks/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTask));

        response.andExpect(status().isBadRequest())
                .andDo(print()).andExpect(content().string("Status has to be 0 or 1!"));

        taskDTO.setTaskStatus(99);
        jsonTask = mapper.writeValueAsString(taskDTO);

        response = mockMvc.perform(post("/tasks/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTask));

        response.andExpect(status().isBadRequest())
                .andDo(print()).andExpect(content().string("Status has to be 0 or 1!"));

        taskDTO.setTaskStatus(1);
        taskDTO.setTaskStartTime(LocalDateTime.of(LocalDate.parse("2022-12-25"), LocalTime.parse("19:45:00")));
        taskDTO.setTaskEndTime(LocalDateTime.of(LocalDate.parse("2022-12-25"), LocalTime.parse("15:00:00")));
        jsonTask = mapper.writeValueAsString(taskDTO);

        response = mockMvc.perform(post("/tasks/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTask));

        response.andExpect(status().isBadRequest())
                .andDo(print()).andExpect(content().string("End time is before the start time!"));

        taskDTO.setTaskStartTime(LocalDateTime.of(LocalDate.parse("2022-12-25"), LocalTime.parse("12:00:00")));
        taskDTO.setCategoryName("Test");

        jsonTask = mapper.writeValueAsString(taskDTO);

        response = mockMvc.perform(post("/tasks/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTask));

        response.andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("categoryName", is("Other")));

        taskDTO.setCategoryName("");

        jsonTask = mapper.writeValueAsString(taskDTO);

        response = mockMvc.perform(post("/tasks/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTask));

        response.andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("categoryName", is("Other")));

        taskDTO.setCategoryName(" ");

        jsonTask = mapper.writeValueAsString(taskDTO);

        response = mockMvc.perform(post("/tasks/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTask));

        response.andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("categoryName", is("Other")));

        taskDTO.setCategoryName("123456");

        jsonTask = mapper.writeValueAsString(taskDTO);

        response = mockMvc.perform(post("/tasks/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTask));

        response.andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("categoryName", is("Other")));

        taskDTO.setCategoryName(null);

        jsonTask = mapper.writeValueAsString(taskDTO);

        response = mockMvc.perform(post("/tasks/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTask));

        response.andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("categoryName", is("Other")));
    }

    @Test
    public void badDataUpdateTest() throws Exception {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTaskName("Control the world");
        taskDTO.setTaskOrder(1);
        taskDTO.setTaskNote("Testing testing 123!");
        taskDTO.setTaskStatus(1);
        taskDTO.setTaskStartTime(LocalDateTime.of(LocalDate.parse("2022-12-25"), LocalTime.parse("15:00:00")));
        taskDTO.setTaskEndTime(LocalDateTime.of(LocalDate.parse("2022-12-25"), LocalTime.parse("19:35:00")));
        taskDTO.setTaskDate(LocalDate.parse("2022-12-25"));
        taskDTO.setCategoryName("Medical");

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        String jsonTask = mapper.writeValueAsString(taskDTO);

        ResultActions createResponse = mockMvc.perform(post("/tasks/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTask));

        String originalUUID = JsonPath.read(createResponse.andReturn().getResponse().getContentAsString(), "taskUUID");
        taskDTO.setTaskUUID(UUID.fromString(originalUUID));

        createResponse.andExpect(status().isCreated())
                .andDo(print())
                .andExpect(jsonPath("taskName", is("Control the world")))
                .andExpect(jsonPath("taskOrder", is(1)))
                .andExpect(jsonPath("taskNote", is("Testing testing 123!")))
                .andExpect(jsonPath("taskStatus", is(1)))
                .andExpect(jsonPath("taskStartTime", is("2022-12-25 15:00:00")))
                .andExpect(jsonPath("taskEndTime", is("2022-12-25 19:35:00")))
                .andExpect(jsonPath("taskDate", is("2022-12-25")))
                .andExpect(jsonPath("categoryName", is("Medical")));

        taskDTO.setTaskName("g6ERzuQC9uSJ2K5gjgOxE8JwpfdxHEMgObOH0K9pXRx9EFZQbvbcvbcbve9MV");
        String updateJsonTask = mapper.writeValueAsString(taskDTO);

        ResultActions updateResponse = mockMvc.perform(put("/tasks/" + taskDTO.getTaskUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJsonTask));

        updateResponse.andExpect(status().isBadRequest())
                .andDo(print())
                .andExpect(content().string("Name must be between 2 and 50 characters!"));

        taskDTO.setTaskName("");
        updateJsonTask = mapper.writeValueAsString(taskDTO);

        updateResponse = mockMvc.perform(put("/tasks/" + taskDTO.getTaskUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJsonTask));

        updateResponse.andExpect(status().isBadRequest())
                .andDo(print()).andExpect(content().string("Name must be between 2 and 50 characters!"));

        taskDTO.setTaskName(null);
        updateJsonTask = mapper.writeValueAsString(taskDTO);

        updateResponse = mockMvc.perform(put("/tasks/" + taskDTO.getTaskUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJsonTask));

        updateResponse.andExpect(status().isBadRequest())
                .andDo(print()).andExpect(content().string("Name must be between 2 and 50 characters!"));

        taskDTO.setTaskName(" ");
        updateJsonTask = mapper.writeValueAsString(taskDTO);

        updateResponse = mockMvc.perform(put("/tasks/" + taskDTO.getTaskUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJsonTask));

        updateResponse.andExpect(status().isBadRequest())
                .andDo(print()).andExpect(content().string("Name must be between 2 and 50 characters!"));

        taskDTO.setTaskName("A");
        updateJsonTask = mapper.writeValueAsString(taskDTO);

        updateResponse = mockMvc.perform(put("/tasks/" + taskDTO.getTaskUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJsonTask));

        updateResponse.andExpect(status().isBadRequest())
                .andDo(print()).andExpect(content().string("Name must be between 2 and 50 characters!"));

        taskDTO.setTaskName("Update the world");
        taskDTO.setTaskOrder(0);
        updateJsonTask = mapper.writeValueAsString(taskDTO);

        updateResponse = mockMvc.perform(put("/tasks/" + taskDTO.getTaskUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJsonTask));

        updateResponse.andExpect(status().isBadRequest())
                .andDo(print()).andExpect(content().string("Invalid order value!"));

        taskDTO.setTaskOrder(-1);
        updateJsonTask = mapper.writeValueAsString(taskDTO);

        updateResponse = mockMvc.perform(put("/tasks/" + taskDTO.getTaskUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJsonTask));

        updateResponse.andExpect(status().isBadRequest())
                .andDo(print()).andExpect(content().string("Invalid order value!"));

        taskDTO.setTaskOrder(null);
        updateJsonTask = mapper.writeValueAsString(taskDTO);

        updateResponse = mockMvc.perform(put("/tasks/" + taskDTO.getTaskUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJsonTask));

        updateResponse.andExpect(status().isBadRequest())
                .andDo(print()).andExpect(content().string("Invalid order value!"));

        taskDTO.setTaskOrder(1);
        taskDTO.setTaskNote("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec qua");
        updateJsonTask = mapper.writeValueAsString(taskDTO);

        updateResponse = mockMvc.perform(put("/tasks/" + taskDTO.getTaskUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJsonTask));

        updateResponse.andExpect(status().isBadRequest())
                .andDo(print()).andExpect(content().string("Note cannot be longer than 200 characters!"));

        taskDTO.setTaskNote("Testing testing 123!");
        taskDTO.setTaskStatus(null);
        updateJsonTask = mapper.writeValueAsString(taskDTO);

        updateResponse = mockMvc.perform(put("/tasks/" + taskDTO.getTaskUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJsonTask));

        updateResponse.andExpect(status().isBadRequest())
                .andDo(print()).andExpect(content().string("Status cannot be null!"));

        taskDTO.setTaskStatus(-1);
        updateJsonTask = mapper.writeValueAsString(taskDTO);

        updateResponse = mockMvc.perform(put("/tasks/" + taskDTO.getTaskUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJsonTask));

        updateResponse.andExpect(status().isBadRequest())
                .andDo(print()).andExpect(content().string("Status has to be 0 or 1!"));

        taskDTO.setTaskStatus(-99);
        updateJsonTask = mapper.writeValueAsString(taskDTO);

        updateResponse = mockMvc.perform(put("/tasks/" + taskDTO.getTaskUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJsonTask));

        updateResponse.andExpect(status().isBadRequest())
                .andDo(print()).andExpect(content().string("Status has to be 0 or 1!"));

        taskDTO.setTaskStatus(2);
        updateJsonTask = mapper.writeValueAsString(taskDTO);

        updateResponse = mockMvc.perform(put("/tasks/" + taskDTO.getTaskUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJsonTask));

        updateResponse.andExpect(status().isBadRequest())
                .andDo(print()).andExpect(content().string("Status has to be 0 or 1!"));

        taskDTO.setTaskStatus(99);
        updateJsonTask = mapper.writeValueAsString(taskDTO);

        updateResponse = mockMvc.perform(put("/tasks/" + taskDTO.getTaskUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJsonTask));

        updateResponse.andExpect(status().isBadRequest())
                .andDo(print()).andExpect(content().string("Status has to be 0 or 1!"));

        taskDTO.setTaskStatus(1);
        taskDTO.setTaskStartTime(LocalDateTime.of(LocalDate.parse("2022-12-25"), LocalTime.parse("19:00:00")));
        taskDTO.setTaskEndTime(LocalDateTime.of(LocalDate.parse("2022-12-25"), LocalTime.parse("15:00:00")));
        updateJsonTask = mapper.writeValueAsString(taskDTO);

        updateResponse = mockMvc.perform(put("/tasks/" + taskDTO.getTaskUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJsonTask));

        updateResponse.andExpect(status().isBadRequest())
                .andDo(print()).andExpect(content().string("End time is before the start time!"));

        taskDTO.setTaskStartTime(LocalDateTime.of(LocalDate.parse("2022-12-25"), LocalTime.parse("12:00:00")));
        taskDTO.setCategoryName("Test");

        updateJsonTask = mapper.writeValueAsString(taskDTO);

        updateResponse = mockMvc.perform(put("/tasks/" + taskDTO.getTaskUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJsonTask));

        updateResponse.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("categoryName", is("Other")));

        taskDTO.setCategoryName("");

        updateJsonTask = mapper.writeValueAsString(taskDTO);

        updateResponse = mockMvc.perform(put("/tasks/" + taskDTO.getTaskUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJsonTask));

        updateResponse.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("categoryName", is("Other")));

        taskDTO.setCategoryName(" ");

        updateJsonTask = mapper.writeValueAsString(taskDTO);

        updateResponse = mockMvc.perform(put("/tasks/" + taskDTO.getTaskUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJsonTask));

        updateResponse.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("categoryName", is("Other")));

        taskDTO.setCategoryName("123456");

        updateJsonTask = mapper.writeValueAsString(taskDTO);

        updateResponse = mockMvc.perform(put("/tasks/" + taskDTO.getTaskUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJsonTask));

        updateResponse.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("categoryName", is("Other")));

        taskDTO.setCategoryName(null);

        updateJsonTask = mapper.writeValueAsString(taskDTO);

        updateResponse = mockMvc.perform(put("/tasks/" + taskDTO.getTaskUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJsonTask));

        updateResponse.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("categoryName", is("Other")));

        ResultActions deleteResponse = mockMvc.perform(delete("/tasks/" + taskDTO.getTaskUUID()));
        deleteResponse.andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string(taskDTO.getTaskName() + " has been deleted successfully!"));
    }
}

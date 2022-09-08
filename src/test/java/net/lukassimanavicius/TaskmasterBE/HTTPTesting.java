package net.lukassimanavicius.TaskmasterBE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.lukassimanavicius.TaskmasterBE.dto.TaskDTO;
import net.lukassimanavicius.TaskmasterBE.services.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    public void GetSingleCategoryTest() throws Exception {
        ResultActions response = mockMvc.perform(get("/categories/{id}", 1));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("categoryName", is("Work")));
    }

    @Test
    public void GetSingleBadCategoryTest() throws Exception {
        ResultActions response = mockMvc.perform(get("/categories/{id}", 99));

        response.andExpect(status().isBadRequest())
                .andDo(print()).andExpect(content().string("Bad id, please try again."));
    }

    @Test
    public void GetSingleEmptyCategoryTest() throws Exception {
        ResultActions response = mockMvc.perform(get("/categories/{id}", " "));

        response.andExpect(status().isBadRequest())
                .andDo(print()).andExpect(content().string("Bad id, please try again."));
    }

    @Test
    public void GetSingleNotNumberCategoryTest() throws Exception {
        ResultActions response = mockMvc.perform(get("/categories/{id}", "c"));

        response.andExpect(status().isBadRequest())
                .andDo(print()).andExpect(content().string("Bad id, please try again."));
    }

    @Test
    public void GetSeveralSingleCategoryTest() throws Exception {
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
    public void GetAllCategoryTest() throws Exception {
        ResultActions response = mockMvc.perform(get("/categories"));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(12)));
    }


    @Test
    public void GetSingleTaskTest() throws Exception {
        ResultActions response = mockMvc.perform(get("/tasks/{id}", "985c1013-2f2f-11ed-ac36-fcaa14e3878f"));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("taskUUID", is("985c1013-2f2f-11ed-ac36-fcaa14e3878f")))
                .andExpect(jsonPath("taskName", is("Wash up")))
                .andExpect(jsonPath("taskOrder", is(1)))
                .andExpect(jsonPath("taskNote", is("")))
                .andExpect(jsonPath("taskStatus", is("Completed")))
                .andExpect(jsonPath("taskStartTime", is("08:00:00")))
                .andExpect(jsonPath("taskEndTime", is("08:15:00")))
                .andExpect(jsonPath("taskDate", is("2022-08-28")))
                .andExpect(jsonPath("categoryName", is("Beauty")));
    }

    @Test
    public void GetSingleBadTaskTest() throws Exception {
        ResultActions response = mockMvc.perform(get("/tasks/{id}", 99));

        response.andExpect(status().isBadRequest())
                .andDo(print()).andExpect(content().string("Bad id, please try again."));
    }

    @Test
    public void GetSingleRandomIDTaskTest() throws Exception {
        UUID randomID = UUID.randomUUID();
        System.out.println(randomID);
        ResultActions response = mockMvc.perform(get("/tasks/{id}", randomID));

        response.andExpect(status().isBadRequest())
                .andDo(print()).andExpect(content().string("Bad id, please try again."));
    }

    @Test
    public void GetSingleEmptyTaskTest() throws Exception {
        ResultActions response = mockMvc.perform(get("/tasks/{id}", " "));

        response.andExpect(status().isBadRequest())
                .andDo(print()).andExpect(content().string("Bad id, please try again."));
    }

    @Test
    public void GetSingleNotNumberTaskTest() throws Exception {
        ResultActions response = mockMvc.perform(get("/tasks/{id}", "c"));

        response.andExpect(status().isBadRequest())
                .andDo(print()).andExpect(content().string("Bad id, please try again."));
    }

    @Test
    public void GetSeveralSingleTaskTest() throws Exception {
        ResultActions response1 = mockMvc.perform(get("/tasks/{id}", "985da9bb-2f2f-11ed-ac36-fcaa14e3878f")); //2
        ResultActions response2 = mockMvc.perform(get("/tasks/{id}", "98691da6-2f2f-11ed-ac36-fcaa14e3878f")); //8
        ResultActions response3 = mockMvc.perform(get("/tasks/{id}", "9872c7ee-2f2f-11ed-ac36-fcaa14e3878f")); //14
        ResultActions response4 = mockMvc.perform(get("/tasks/{id}", "987e26f7-2f2f-11ed-ac36-fcaa14e3878f")); //20
        ResultActions response5 = mockMvc.perform(get("/tasks/{id}", "98934154-2f2f-11ed-ac36-fcaa14e3878f")); //26


        response1.andExpect(status().isOk())
                .andExpect(jsonPath("taskUUID", is("985da9bb-2f2f-11ed-ac36-fcaa14e3878f")))
                .andExpect(jsonPath("taskName", is("Breakfast")))
                .andExpect(jsonPath("taskOrder", is(2)))
                .andExpect(jsonPath("taskNote", is("Overnight oats")))
                .andExpect(jsonPath("taskStatus", is("Completed")))
                .andExpect(jsonPath("taskStartTime", is("08:15:00")))
                .andExpect(jsonPath("taskEndTime", is("08:30:00")))
                .andExpect(jsonPath("taskDate", is("2022-08-28")))
                .andExpect(jsonPath("categoryName", is("Food")));

        response2.andExpect(status().isOk())
                .andExpect(jsonPath("taskUUID", is("98691da6-2f2f-11ed-ac36-fcaa14e3878f")))
                .andExpect(jsonPath("taskName", is("Learning")))
                .andExpect(jsonPath("taskOrder", is(8)))
                .andExpect(jsonPath("taskNote", is("Algos chapter 11")))
                .andExpect(jsonPath("taskStatus", is("")))
                .andExpect(jsonPath("taskStartTime", is("20:30:00")))
                .andExpect(jsonPath("taskEndTime", is("22:00:00")))
                .andExpect(jsonPath("taskDate", is("2022-08-28")))
                .andExpect(jsonPath("categoryName", is("Education")));

        response3.andExpect(status().isOk())
                .andExpect(jsonPath("taskUUID", is("9872c7ee-2f2f-11ed-ac36-fcaa14e3878f")))
                .andExpect(jsonPath("taskName", is("Task3")))
                .andExpect(jsonPath("taskOrder", is(3)))
                .andExpect(jsonPath("taskNote", is("Random note")))
                .andExpect(jsonPath("taskStatus").doesNotExist())
                .andExpect(jsonPath("taskStartTime").doesNotExist())
                .andExpect(jsonPath("taskEndTime").doesNotExist())
                .andExpect(jsonPath("taskDate", is("2022-08-29")))
                .andExpect(jsonPath("categoryName", is("Other")));

        response4.andExpect(status().isOk())
                .andExpect(jsonPath("taskUUID", is("987e26f7-2f2f-11ed-ac36-fcaa14e3878f")))
                .andExpect(jsonPath("taskName", is("Task4")))
                .andExpect(jsonPath("taskOrder", is(1)))
                .andExpect(jsonPath("taskNote").doesNotExist())
                .andExpect(jsonPath("taskStatus").doesNotExist())
                .andExpect(jsonPath("taskStartTime").doesNotExist())
                .andExpect(jsonPath("taskEndTime").doesNotExist())
                .andExpect(jsonPath("taskDate", is("2022-09-20")))
                .andExpect(jsonPath("categoryName", is("Entertainment")));


        LocalDate date = LocalDate.now();
        response5.andExpect(status().isOk())
                .andExpect(jsonPath("taskUUID", is("98934154-2f2f-11ed-ac36-fcaa14e3878f")))
                .andExpect(jsonPath("taskName", is("Task1")))
                .andExpect(jsonPath("taskOrder", is(1)))
                .andExpect(jsonPath("taskNote").doesNotExist())
                .andExpect(jsonPath("taskStatus").doesNotExist())
                .andExpect(jsonPath("taskStartTime").doesNotExist())
                .andExpect(jsonPath("taskEndTime").doesNotExist())
                .andExpect(jsonPath("taskDate", is(date.toString())))
                .andExpect(jsonPath("categoryName", is("Other")));
    }

    @Test
    public void GetAllTasksTest() throws Exception {
        ResultActions response = mockMvc.perform(get("/tasks"));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(26)));
    }

    @Test
    public void CreateNewTaskMinimalTest() throws Exception {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTaskName("Control the world");
        taskDTO.setTaskOrder(1);
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
                .andExpect(jsonPath("taskStatus").doesNotExist())
                .andExpect(jsonPath("taskStartTime").doesNotExist())
                .andExpect(jsonPath("taskEndTime").doesNotExist())
                .andExpect(jsonPath("taskDate", is("2022-12-25")))
                .andExpect(jsonPath("categoryName", is("Other")));
    }

    @Test
    public void CreateNewTaskFullTest() throws Exception {

        //TODO: Finish this
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTaskName("Control the world");
        taskDTO.setTaskOrder(1);
        taskDTO.setTaskDate(LocalDate.now());

        ResultActions response = mockMvc.perform(post("/save"));

        response.andExpect(status().isCreated())
                .andDo(print())
                .andExpect(content().string("Bad id, please try again."));
    }
}

package net.lukassimanavicius.TaskmasterBE;

import net.lukassimanavicius.TaskmasterBE.services.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
}

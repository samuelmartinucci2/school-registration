package io.metadata.school.registration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import io.metadata.school.registration.Application;
import io.metadata.school.registration.dto.CourseDto;
import io.metadata.school.registration.repository.CourseRepository;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CourseApiRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseRepository repository;

    @BeforeEach
    public void setup() {
        repository.deleteAll();
    }

    @Test
    public void shouldCreateRecord() throws Exception {
        CourseDto course = new CourseDto();
        course.setName(RandomString.make(10));
        ObjectMapper objectMapper = new ObjectMapper();

        String requestJson = objectMapper.writeValueAsString(course);
        MockHttpServletResponse response = mockMvc.perform(post("/course")
                        .contentType("application/json")
                        .content(requestJson))
                .andDo(print())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$.id", greaterThan(0)))
                .andExpect(jsonPath("$.name").value(course.getName()))
                .andExpect(status().isCreated()).andReturn().getResponse();

        Integer id = JsonPath.parse(response.getContentAsString()).read("$.id");
        assertEquals(true, repository.findById(id).isPresent());

    }

    @Test
    public void shouldGetAll() throws Exception {
        CourseDto course = new CourseDto();
        course.setName(RandomString.make(10));
        ObjectMapper objectMapper = new ObjectMapper();

        String requestJson = objectMapper.writeValueAsString(course);
        MockHttpServletResponse response = mockMvc.perform(post("/course")
                        .contentType("application/json")
                        .content(requestJson))
                .andDo(print())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$.id", greaterThan(0)))
                .andExpect(jsonPath("$.name").value(course.getName()))
                .andExpect(status().isCreated()).andReturn().getResponse();
        Integer firstId = JsonPath.parse(response.getContentAsString()).read("$.id");

        response = mockMvc.perform(post("/course")
                        .contentType("application/json")
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isCreated()).andReturn().getResponse();
        Integer secondId = JsonPath.parse(response.getContentAsString()).read("$.id");


        mockMvc.perform(get("/course")
                        .contentType("application/json"))
                .andDo(print())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(firstId))
                .andExpect(jsonPath("$[1].id").value(secondId))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldGetById() throws Exception {
        CourseDto course = new CourseDto();
        course.setName(RandomString.make(10));
        ObjectMapper objectMapper = new ObjectMapper();

        String requestJson = objectMapper.writeValueAsString(course);
        MockHttpServletResponse response = mockMvc.perform(post("/course")
                        .contentType("application/json")
                        .content(requestJson))
                .andDo(print())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$.id", greaterThan(0)))
                .andExpect(jsonPath("$.name").value(course.getName()))
                .andExpect(status().isCreated()).andReturn().getResponse();
        Integer id = JsonPath.parse(response.getContentAsString()).read("$.id");

        mockMvc.perform(get("/course/" + id))
                .andDo(print())
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(course.getName()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/course/" + Integer.MAX_VALUE))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldDeleteById() throws Exception {
        CourseDto course = new CourseDto();
        course.setName(RandomString.make(10));
        ObjectMapper objectMapper = new ObjectMapper();

        String requestJson = objectMapper.writeValueAsString(course);
        MockHttpServletResponse response = mockMvc.perform(post("/course")
                        .contentType("application/json")
                        .content(requestJson))
                .andDo(print())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$.id", greaterThan(0)))
                .andExpect(jsonPath("$.name").value(course.getName()))
                .andExpect(status().isCreated()).andReturn().getResponse();
        Integer id = JsonPath.parse(response.getContentAsString()).read("$.id");

        mockMvc.perform(delete("/course/" + id))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertEquals(false, repository.findById(id).isPresent());

        mockMvc.perform(delete("/course" + Integer.MAX_VALUE))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}

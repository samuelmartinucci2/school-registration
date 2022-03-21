package io.metadata.school.registration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import io.metadata.school.registration.Application;
import io.metadata.school.registration.dto.StudentDto;
import io.metadata.school.registration.repository.StudentRepository;
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
class StudentApiRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository repository;

    @BeforeEach
    public void setup() {
        repository.deleteAll();
    }

    @Test
    public void shouldCreateRecord() throws Exception {
        StudentDto student = new StudentDto();
        student.setName(RandomString.make(10));
        ObjectMapper objectMapper = new ObjectMapper();

        String requestJson = objectMapper.writeValueAsString(student);
        MockHttpServletResponse response = mockMvc.perform(post("/student")
                        .contentType("application/json")
                        .content(requestJson))
                .andDo(print())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$.id", greaterThan(0)))
                .andExpect(jsonPath("$.name").value(student.getName()))
                .andExpect(status().isCreated()).andReturn().getResponse();

        Integer id = JsonPath.parse(response.getContentAsString()).read("$.id");
        assertEquals(true, repository.findById(id).isPresent());

    }

    @Test
    public void shouldGetAll() throws Exception {
        StudentDto student = new StudentDto();
        student.setName(RandomString.make(10));
        ObjectMapper objectMapper = new ObjectMapper();

        String requestJson = objectMapper.writeValueAsString(student);
        MockHttpServletResponse response = mockMvc.perform(post("/student")
                        .contentType("application/json")
                        .content(requestJson))
                .andDo(print())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$.id", greaterThan(0)))
                .andExpect(jsonPath("$.name").value(student.getName()))
                .andExpect(status().isCreated()).andReturn().getResponse();
        Integer firstId = JsonPath.parse(response.getContentAsString()).read("$.id");

        response = mockMvc.perform(post("/student")
                        .contentType("application/json")
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isCreated()).andReturn().getResponse();
        Integer secondId = JsonPath.parse(response.getContentAsString()).read("$.id");


        mockMvc.perform(get("/student")
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
        StudentDto student = new StudentDto();
        student.setName(RandomString.make(10));
        ObjectMapper objectMapper = new ObjectMapper();

        String requestJson = objectMapper.writeValueAsString(student);
        MockHttpServletResponse response = mockMvc.perform(post("/student")
                        .contentType("application/json")
                        .content(requestJson))
                .andDo(print())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$.id", greaterThan(0)))
                .andExpect(jsonPath("$.name").value(student.getName()))
                .andExpect(status().isCreated()).andReturn().getResponse();
        Integer id = JsonPath.parse(response.getContentAsString()).read("$.id");

        mockMvc.perform(get("/student/" + id))
                .andDo(print())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value(student.getName()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/student/" + Integer.MAX_VALUE))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldDeleteById() throws Exception {
        StudentDto student = new StudentDto();
        student.setName(RandomString.make(10));
        ObjectMapper objectMapper = new ObjectMapper();

        String requestJson = objectMapper.writeValueAsString(student);
        MockHttpServletResponse response = mockMvc.perform(post("/student")
                        .contentType("application/json")
                        .content(requestJson))
                .andDo(print())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$.id", greaterThan(0)))
                .andExpect(jsonPath("$.name").value(student.getName()))
                .andExpect(status().isCreated()).andReturn().getResponse();
        Integer id = JsonPath.parse(response.getContentAsString()).read("$.id");

        mockMvc.perform(delete("/student/" + id))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertEquals(false, repository.findById(id).isPresent());

        mockMvc.perform(delete("/student" + Integer.MAX_VALUE))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}

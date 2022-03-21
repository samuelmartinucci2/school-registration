package io.metadata.school.registration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import io.metadata.school.registration.Application;
import io.metadata.school.registration.dto.EnrollmentDto;
import io.metadata.school.registration.entity.Course;
import io.metadata.school.registration.entity.Student;
import io.metadata.school.registration.repository.CourseRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EnrollmentApiRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    public void setup() {
        courseRepository.deleteAll();
        studentRepository.deleteAll();
    }

    @Test
    public void shouldCreateEnrollment() throws Exception {
        int studentId = createStudent(), courseId = createCourse();
        EnrollmentDto enroll = new EnrollmentDto(studentId, courseId);
        ObjectMapper objectMapper = new ObjectMapper();

        String requestJson = objectMapper.writeValueAsString(enroll);
        mockMvc.perform(post("/enrollment/subscribe")
                        .contentType("application/json")
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isNoContent()).andReturn().getResponse();

        mockMvc.perform(get("/course/" + courseId))
                .andDo(print())
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$.students", hasSize(1)))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnCourseListForNoSubscriptionFilter() throws Exception {
        int studentId = createStudent(), courseId = createCourse();
        EnrollmentDto enroll = new EnrollmentDto(studentId, courseId);
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(get("/course/enrollment/none"))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(status().isOk());

        String requestJson = objectMapper.writeValueAsString(enroll);
        mockMvc.perform(post("/enrollment/subscribe")
                        .contentType("application/json")
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isNoContent()).andReturn().getResponse();

        mockMvc.perform(get("/course/enrollment/none"))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(0)))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnStudentListForNoSubscriptionFilter() throws Exception {
        int studentId = createStudent(), courseId = createCourse();
        EnrollmentDto enroll = new EnrollmentDto(studentId, courseId);
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(get("/student/enrollment/none"))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(status().isOk());

        String requestJson = objectMapper.writeValueAsString(enroll);
        mockMvc.perform(post("/enrollment/subscribe")
                        .contentType("application/json")
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isNoContent()).andReturn().getResponse();

        mockMvc.perform(get("/student/enrollment/none"))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(0)))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnCourseListForSubscriptionFilter() throws Exception {
        int studentId = createStudent(), courseId = createCourse();
        EnrollmentDto enroll = new EnrollmentDto(studentId, courseId);
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(get("/course/enrollment/" + studentId))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(0)))
                .andExpect(status().isOk());

        String requestJson = objectMapper.writeValueAsString(enroll);
        mockMvc.perform(post("/enrollment/subscribe")
                        .contentType("application/json")
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isNoContent()).andReturn().getResponse();

        mockMvc.perform(get("/course/enrollment/" + studentId))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnStudentListForSubscriptionFilter() throws Exception {
        int studentId = createStudent(), courseId = createCourse();
        EnrollmentDto enroll = new EnrollmentDto(studentId, courseId);
        ObjectMapper objectMapper = new ObjectMapper();

        mockMvc.perform(get("/student/enrollment/" + courseId))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(0)))
                .andExpect(status().isOk());

        String requestJson = objectMapper.writeValueAsString(enroll);
        mockMvc.perform(post("/enrollment/subscribe")
                        .contentType("application/json")
                        .content(requestJson))
                .andDo(print())
                .andExpect(status().isNoContent()).andReturn().getResponse();

        mockMvc.perform(get("/student/enrollment/" + courseId))
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(status().isOk());
    }

    private int createCourse() throws Exception {
        Course course = new Course();
        course.setName(RandomString.make(10));
        ObjectMapper objectMapper = new ObjectMapper();

        String requestJson = objectMapper.writeValueAsString(course);
        MockHttpServletResponse response = mockMvc.perform(post("/course")
                        .contentType("application/json")
                        .content(requestJson))
                .andDo(print())
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$.id", greaterThan(0)))
                .andExpect(jsonPath("$.name").value(course.getName()))
                .andExpect(status().isCreated()).andReturn().getResponse();
        return JsonPath.parse(response.getContentAsString()).read("$.id");
    }

    private int createStudent() throws Exception {
        Student student = new Student();
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
        return JsonPath.parse(response.getContentAsString()).read("$.id");
    }
}

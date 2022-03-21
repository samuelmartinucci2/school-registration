package io.metadata.school.registration.service.impl;

import io.metadata.school.registration.entity.Course;
import io.metadata.school.registration.entity.Student;
import io.metadata.school.registration.service.CourseService;
import io.metadata.school.registration.service.StudentService;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.hamcrest.core.CombinableMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;

import static io.metadata.school.registration.service.EnrollmentService.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class EnrollmentServiceImplTest {
    @Mock
    private CourseService courseService;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private EnrollmentServiceImpl service;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldEnroll() {
        int courseId = 1, studentId = 1;
        when(courseService.find(courseId)).thenReturn(new Course());
        when(studentService.find(studentId)).thenReturn(new Student());

        service.enroll(1, 1);
    }

    @Test
    public void shouldFailEnrollWhenInvalidCourseId() {
        int courseId = 1;
        when(courseService.find(courseId)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        thrown.expect(ResponseStatusException.class);
        thrown.expect(CombinableMatcher.both(
                        CoreMatchers.is(CoreMatchers.instanceOf(ResponseStatusException.class)))
                .and(Matchers.hasProperty("status", CoreMatchers.is(HttpStatus.BAD_REQUEST)))
                .and(Matchers.hasProperty("reason", CoreMatchers.is(UNKNOWN_COURSE))));

        service.enroll(1, 1);
    }

    @Test
    public void shouldFailEnrollWhenInvalidStudentId() {
        int courseId = 1, studentId = 1;
        when(courseService.find(courseId)).thenReturn(new Course());
        when(studentService.find(studentId)).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        thrown.expect(ResponseStatusException.class);
        thrown.expect(CombinableMatcher.both(
                        CoreMatchers.is(CoreMatchers.instanceOf(ResponseStatusException.class)))
                .and(Matchers.hasProperty("status", CoreMatchers.is(HttpStatus.BAD_REQUEST)))
                .and(Matchers.hasProperty("reason", CoreMatchers.is(UNKNOWN_STUDENT))));

        service.enroll(1, 1);
    }

    @Test
    public void shouldFailEnrollWhenStudentAlreadyEnrolled() {
        int courseId = 1, studentId = 1;
        final Course course = new Course();
        final Student student = new Student();
        student.setId(studentId);
        course.getStudents().add(student);
        when(courseService.find(courseId)).thenReturn(course);
        when(studentService.find(studentId)).thenReturn(student);

        thrown.expect(ResponseStatusException.class);
        thrown.expect(CombinableMatcher.both(
                        CoreMatchers.is(CoreMatchers.instanceOf(ResponseStatusException.class)))
                .and(Matchers.hasProperty("status", CoreMatchers.is(HttpStatus.BAD_REQUEST)))
                .and(Matchers.hasProperty("reason", CoreMatchers.is(STUDENT_ALREADY_ENROLLED_IN_THE_COURSE))));

        service.enroll(1, 1);
    }

    @Test
    public void shouldFailEnrollWhenStudentEnrolledInTooManyCourses() {
        int courseId = 1, studentId = 1;
        final Student student = new Student();
        student.setId(studentId);
        student.setCourses(Arrays.asList(new Course(), new Course(), new Course(), new Course(), new Course()));
        when(courseService.find(courseId)).thenReturn(new Course());
        when(studentService.find(studentId)).thenReturn(student);

        thrown.expect(ResponseStatusException.class);
        thrown.expect(CombinableMatcher.both(
                        CoreMatchers.is(CoreMatchers.instanceOf(ResponseStatusException.class)))
                .and(Matchers.hasProperty("status", CoreMatchers.is(HttpStatus.BAD_REQUEST)))
                .and(Matchers.hasProperty("reason", CoreMatchers.is(STUDENT_IS_ALREADY_ENROLLED_IN_TOO_MANY_COURSES))));

        service.enroll(1, 1);
    }

    @Test
    public void shouldFailEnrollWhenCourseIsFull() {
        int courseId = 1, studentId = 1;
        final Course course = new Course();
        for (int id = 1; id <= 50; id++) {
            final Student student = new Student();
            student.setId(id);
            course.getStudents().add(student);
        }
        when(courseService.find(courseId)).thenReturn(course);
        when(studentService.find(studentId)).thenReturn(new Student());

        thrown.expect(ResponseStatusException.class);
        thrown.expect(CombinableMatcher.both(
                        CoreMatchers.is(CoreMatchers.instanceOf(ResponseStatusException.class)))
                .and(Matchers.hasProperty("status", CoreMatchers.is(HttpStatus.BAD_REQUEST)))
                .and(Matchers.hasProperty("reason", CoreMatchers.is(COURSE_IS_FULL))));

        service.enroll(1, 1);
    }
}

package io.metadata.school.registration.service.impl;

import io.metadata.school.registration.entity.Course;
import io.metadata.school.registration.repository.CourseRepository;
import net.bytebuddy.utility.RandomString;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CourseServiceImplTest {
    @Mock
    private CourseRepository repository;

    @InjectMocks
    private CourseServiceImpl service;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldCreate() {
        when(repository.save(any())).then(returnsFirstArg());

        final Course course = new Course();
        course.setName(RandomString.make(10));

        final Course added = service.add(course);

        verify(repository).save(course);
        assertEquals(course, added);
    }

    @Test
    public void shouldUpdateWhenRecordExists() {
        int courseId = 1;
        when(repository.findById(courseId)).thenReturn(Optional.of(new Course()));
        when(repository.save(any())).then(returnsFirstArg());

        final Course course = new Course();
        course.setName(RandomString.make(10));

        final Course updated = service.update(courseId, course);

        verify(repository).save(course);
        assertEquals(course, updated);
    }

    @Test
    public void shouldFailUpdateWhenCourseIdDoesNotExist() {
        int courseId = 1;
        when(repository.findById(courseId)).thenReturn(Optional.empty());
        thrown.expect(ResponseStatusException.class);

        service.update(courseId, new Course());

        verify(repository, never()).save(any());
    }

    @Test
    public void shouldListAllCourses() {
        service.list();

        verify(repository).findAll();
    }

    @Test
    public void shouldFindWhenRecordExists() {
        int courseId = 1;
        final Course course = new Course();
        course.setName(RandomString.make(10));
        when(repository.findById(courseId)).thenReturn(Optional.of(course));

        final Course found = service.find(courseId);

        verify(repository).findById(courseId);
        assertEquals(course, found);
    }

    @Test
    public void shouldFailFindWhenCourseIdDoesNotExist() {
        int courseId = 1;
        when(repository.findById(courseId)).thenReturn(Optional.empty());
        thrown.expect(ResponseStatusException.class);

        service.find(courseId);
    }

    @Test
    public void shouldDeleteWhenRecordExists() {
        int courseId = 1;
        final Course course = new Course();
        course.setName(RandomString.make(10));
        when(repository.findById(courseId)).thenReturn(Optional.of(course));

        service.delete(courseId);

        verify(repository).delete(any());
    }

    @Test
    public void shouldFailDeleteWhenCourseIdDoesNotExist() {
        int courseId = 1;
        when(repository.findById(courseId)).thenReturn(Optional.empty());
        thrown.expect(ResponseStatusException.class);

        service.delete(courseId);

        verify(repository, never()).delete(any());
    }

    @Test
    public void shouldListCoursesByStudentId() {
        int studentId = 1;
        service.findByStudent(studentId);

        verify(repository).findByStudents_Id(studentId);
    }

    @Test
    public void shouldListCoursesWithNoEnrollment() {
        service.findNoEnrollment();

        verify(repository).findNoEnrollmentCourses();
    }
}

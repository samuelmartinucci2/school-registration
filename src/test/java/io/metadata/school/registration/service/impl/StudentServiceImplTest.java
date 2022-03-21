package io.metadata.school.registration.service.impl;

import io.metadata.school.registration.entity.Student;
import io.metadata.school.registration.repository.StudentRepository;
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
public class StudentServiceImplTest {
    @Mock
    private StudentRepository repository;

    @InjectMocks
    private StudentServiceImpl service;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldCreate() {
        when(repository.save(any())).then(returnsFirstArg());

        final Student student = new Student();
        student.setName(RandomString.make(10));

        final Student added = service.add(student);

        verify(repository).save(student);
        assertEquals(student, added);
    }

    @Test
    public void shouldUpdateWhenRecordExists() {
        int studentId = 1;
        when(repository.findById(studentId)).thenReturn(Optional.of(new Student()));
        when(repository.save(any())).then(returnsFirstArg());

        final Student student = new Student();
        student.setName(RandomString.make(10));

        final Student updated = service.update(studentId, student);

        verify(repository).save(student);
        assertEquals(student, updated);
    }

    @Test
    public void shouldFailUpdateWhenStudentIdDoesNotExist() {
        int studentId = 1;
        when(repository.findById(studentId)).thenReturn(Optional.empty());
        thrown.expect(ResponseStatusException.class);

        service.update(studentId, new Student());

        verify(repository, never()).save(any());
    }

    @Test
    public void shouldListAllStudents() {
        service.list();

        verify(repository).findAll();
    }

    @Test
    public void shouldFindWhenRecordExists() {
        int studentId = 1;
        final Student student = new Student();
        student.setName(RandomString.make(10));
        when(repository.findById(studentId)).thenReturn(Optional.of(student));

        final Student found = service.find(studentId);

        verify(repository).findById(studentId);
        assertEquals(student, found);
    }

    @Test
    public void shouldFailFindWhenStudentIdDoesNotExist() {
        int studentId = 1;
        when(repository.findById(studentId)).thenReturn(Optional.empty());
        thrown.expect(ResponseStatusException.class);

        service.find(studentId);
    }

    @Test
    public void shouldDeleteWhenRecordExists() {
        int studentId = 1;
        final Student student = new Student();
        student.setName(RandomString.make(10));
        when(repository.findById(studentId)).thenReturn(Optional.of(student));

        service.delete(studentId);

        verify(repository).delete(any());
    }

    @Test
    public void shouldFailDeleteWhenStudentIdDoesNotExist() {
        int studentId = 1;
        when(repository.findById(studentId)).thenReturn(Optional.empty());
        thrown.expect(ResponseStatusException.class);

        service.delete(studentId);

        verify(repository, never()).delete(any());
    }

    @Test
    public void shouldListStudentsByCourseId() {
        int courseId = 1;
        service.findByCourse(courseId);

        verify(repository).findByCourses_Id(courseId);
    }

    @Test
    public void shouldListStudentsWithNoEnrollment() {
        service.findNonSubscribed();

        verify(repository).findNonSubscribedStudents();
    }
}

package io.metadata.school.registration.entity;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CourseTest {
    @Test
    public void shouldReturnFalseWhenStudentNotEnrolled() {
        final Course course = new Course();
        final Student student = new Student();

        assertFalse(course.isEnrolled(student));
    }

    @Test
    public void shouldReturnTrueWhenStudentEnrolled() {
        final Course course = new Course();
        final Student student = new Student();
        course.getStudents().add(student);

        assertTrue(course.isEnrolled(student));
    }
}

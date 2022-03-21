package io.metadata.school.registration.service.impl;

import io.metadata.school.registration.entity.Course;
import io.metadata.school.registration.entity.Student;
import io.metadata.school.registration.service.CourseService;
import io.metadata.school.registration.service.EnrollmentService;
import io.metadata.school.registration.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    private static final int STUDENT_MAX_ENROLLMENTS = 5;
    private static final int COURSE_MAX_ENROLLMENTS = 50;

    @Autowired
    private final CourseService courseService;
    @Autowired
    private final StudentService studentService;

    @Transactional
    @Override
    public void enroll(final int studentId, final int courseId) {
        final Course course = tryFindCourse(courseId);
        final Student student = tryFindStudent(studentId);
        validateEnrollment(student, course);

        course.getStudents().add(student);
    }

    private void validateEnrollment(final Student student, final Course course) {
        if (course.isEnrolled(student)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, STUDENT_ALREADY_ENROLLED_IN_THE_COURSE);
        }

        if (student.getCourses().size() >= STUDENT_MAX_ENROLLMENTS) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, STUDENT_IS_ALREADY_ENROLLED_IN_TOO_MANY_COURSES);
        }

        if (course.getStudents().size() >= COURSE_MAX_ENROLLMENTS) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, COURSE_IS_FULL);
        }
    }

    private Course tryFindCourse(int id) {
        try {
            return courseService.find(id);
        } catch (ResponseStatusException exception) {
            if (exception.getStatus() == HttpStatus.NOT_FOUND)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, UNKNOWN_COURSE);
            throw exception;
        }
    }

    private Student tryFindStudent(int id) {
        try {
            return studentService.find(id);
        } catch (ResponseStatusException exception) {
            if (exception.getStatus() == HttpStatus.NOT_FOUND)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, UNKNOWN_STUDENT);
            throw exception;
        }
    }
}

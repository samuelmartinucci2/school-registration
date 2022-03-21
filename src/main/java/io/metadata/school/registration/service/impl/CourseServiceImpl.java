package io.metadata.school.registration.service.impl;

import io.metadata.school.registration.entity.Course;
import io.metadata.school.registration.repository.CourseRepository;
import io.metadata.school.registration.service.CourseService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private final CourseRepository repository;

    @Transactional
    @Override
    public Course add(final Course course) {
        course.setId(null);
        return repository.save(course);
    }

    @Transactional
    @Override
    public Course update(final int id, final Course course) {
        final Course updated = find(id);
        updated.setName(course.getName());
        return repository.save(updated);
    }

    @Override
    public List<Course> list() {
        return repository.findAll();
    }

    @Override
    public Course find(final int id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Transactional
    @Override
    public void delete(final int id) {
        final Course weather = find(id);
        repository.delete(weather);
    }

    @Override
    public List<Course> findByStudent(final int studentId) {
        return repository.findByStudents_Id(studentId);
    }

    @Override
    public List<Course> findNoEnrollment() {
        return repository.findNoEnrollmentCourses();
    }
}

package io.metadata.school.registration.service.impl;

import io.metadata.school.registration.entity.Student;
import io.metadata.school.registration.repository.StudentRepository;
import io.metadata.school.registration.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequiredArgsConstructor
@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private final StudentRepository repository;

    @Transactional
    @Override
    public Student add(final Student student) {
        student.setId(null);
        return repository.save(student);
    }

    @Transactional
    @Override
    public Student update(final int id, final Student student) {
        final Student updated = find(id);
        updated.setName(student.getName());
        return repository.save(updated);
    }

    @Override
    public List<Student> list() {
        return repository.findAll();
    }

    @Override
    public Student find(final int id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Transactional
    @Override
    public void delete(final int id) {
        final Student weather = find(id);
        repository.delete(weather);
    }

    @Override
    public List<Student> findByCourse(final int courseId) {
        return repository.findByCourses_Id(courseId);
    }

    @Override
    public List<Student> findNonSubscribed() {
        return repository.findNonSubscribedStudents();
    }
}

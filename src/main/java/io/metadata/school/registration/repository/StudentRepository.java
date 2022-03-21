package io.metadata.school.registration.repository;

import io.metadata.school.registration.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Integer> {
    List<Student> findByCourses_Id(int id);

    @Query("SELECT student FROM Student student WHERE student.courses IS EMPTY")
    List<Student> findNonSubscribedStudents();
}

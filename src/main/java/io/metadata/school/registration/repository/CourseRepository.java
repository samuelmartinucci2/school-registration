package io.metadata.school.registration.repository;

import io.metadata.school.registration.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Integer> {
    List<Course> findByStudents_Id(int id);

    @Query("SELECT course FROM Course course WHERE course.students IS EMPTY")
    List<Course> findNoEnrollmentCourses();
}

package io.metadata.school.registration.service;

import io.metadata.school.registration.entity.Student;

import java.util.List;

public interface StudentService {

    /**
     * Creates a new student
     *
     * @param student - data to be inserted
     * @return newly created student
     */
    Student add(Student student);

    /**
     * Updates a pre-existing student (throw exception in case an invalid ID is provided).
     *
     * @param id      - valid course id
     * @param student - data to be used in the update
     * @return updated student
     */
    Student update(int id, Student student);

    /**
     * List all students
     *
     * @return list of students
     */
    List<Student> list();

    /**
     * Find students by id (throw exception in case an invalid ID is provided).
     *
     * @param id - valid student id
     * @return found student
     */
    Student find(int id);

    /**
     * Delete the specified student (throw exception in case an invalid ID is provided).
     *
     * @param id - valid student id
     */
    void delete(int id);

    /**
     * Find students by course enrolled
     *
     * @param courseId - valid course ID.
     * @return list of students
     */
    List<Student> findByCourse(int courseId);

    /**
     * Find students not subscribed in any course.
     *
     * @return list of students
     */
    List<Student> findNonSubscribed();

}

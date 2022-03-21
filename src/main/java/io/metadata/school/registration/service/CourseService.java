package io.metadata.school.registration.service;

import io.metadata.school.registration.entity.Course;

import java.util.List;

public interface CourseService {

    /**
     * Creates a new course
     *
     * @param course - data to be inserted
     * @return newly created course
     */
    Course add(Course course);

    /**
     * Updates a pre-existing course (throw exception in case an invalid ID is provided).
     *
     * @param id     - valid course id
     * @param course - data to be used in the update
     * @return updated course
     */
    Course update(int id, Course course);

    /**
     * List all courses
     *
     * @return list of courses
     */
    List<Course> list();

    /**
     * Find courses by id (throw exception in case an invalid ID is provided).
     *
     * @param id - valid course id
     * @return found course
     */
    Course find(int id);

    /**
     * Delete the specified course (throw exception in case an invalid ID is provided).
     *
     * @param id - valid course id
     */
    void delete(int id);

    /**
     * Find courses by enrolled student.
     *
     * @param studentId - valid student ID.
     * @return list of courses
     */
    List<Course> findByStudent(final int studentId);

    /**
     * Find courses with no enrollment
     *
     * @return list of courses
     */
    List<Course> findNoEnrollment();

}

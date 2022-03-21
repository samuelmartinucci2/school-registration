package io.metadata.school.registration.service;

public interface EnrollmentService {

    String UNKNOWN_STUDENT = "Unknown student";
    String UNKNOWN_COURSE = "Unknown course";
    String STUDENT_ALREADY_ENROLLED_IN_THE_COURSE = "Student already enrolled in the course";
    String STUDENT_IS_ALREADY_ENROLLED_IN_TOO_MANY_COURSES = "Student is already enrolled in too many courses";
    String COURSE_IS_FULL = "Course is full";

    /**
     * Enroll student into a course
     *
     * @param studentId - valid studentID
     * @param courseId  - valid courseID
     */
    void enroll(int studentId, int courseId);
}

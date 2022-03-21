package io.metadata.school.registration.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @ManyToMany(cascade=CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name="enrollment",
            joinColumns={@JoinColumn(name="course_id")},
            inverseJoinColumns={@JoinColumn(name="student_id")})
    private List<Student> students = new ArrayList<>();

    public boolean isEnrolled(final Student student) {
        return students.stream().filter(found -> found.getId() == student.getId())
                .findAny()
                .isPresent();
    }

}

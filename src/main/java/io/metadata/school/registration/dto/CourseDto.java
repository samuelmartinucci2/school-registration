package io.metadata.school.registration.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CourseDto implements Serializable {
    private int id;
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<StudentDto> students;
}

package io.metadata.school.registration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EnrollmentDto implements Serializable {
    private int studentId;
    private int courseId;
}

package io.metadata.school.registration.controller;


import io.metadata.school.registration.dto.StudentDto;
import io.metadata.school.registration.entity.Student;
import io.metadata.school.registration.service.StudentService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/student")
public class StudentApiRestController {

    @Autowired
    private StudentService service;

    private ModelMapper modelMapper = new ModelMapper();

    @ApiOperation(value = "Creates a new student")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Returns the created student"),
            @ApiResponse(code = 500, message = "Unable to store student")
    })
    @PostMapping
    public ResponseEntity create(@RequestBody final StudentDto dto) {
        final Student student = fromDto(dto);
        final StudentDto created = toDto(service.add(student));
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @ApiOperation(value = "Updates the specified student")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns the updated student"),
            @ApiResponse(code = 404, message = "Unable to find student with the specified id"),
            @ApiResponse(code = 500, message = "Unable to store student")
    })
    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable("id") int id, @RequestBody final StudentDto dto) {
        final Student student = fromDto(dto);
        final StudentDto created = toDto(service.update(id, student));
        return ResponseEntity.status(HttpStatus.OK).body(created);
    }

    @ApiOperation(value = "List all students")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns the list of students"),
            @ApiResponse(code = 500, message = "Unable to list students")
    })
    @GetMapping
    public ResponseEntity list() {
        final List<StudentDto> studentList = service.list().stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok().body(studentList);
    }

    @ApiOperation(value = "Find student by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns the found student"),
            @ApiResponse(code = 404, message = "Unable to find student with the specified id"),
            @ApiResponse(code = 500, message = "Unable to find student")
    })
    @GetMapping("/{id}")
    public ResponseEntity find(@PathVariable("id") int id) {
        final StudentDto dto = toDto(service.find(id));
        return ResponseEntity.ok().body(dto);
    }

    @ApiOperation(value = "Delete student by id")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Empty body indicating deletion worked."),
            @ApiResponse(code = 404, message = "Unable to find student with the specified id"),
            @ApiResponse(code = 500, message = "Unable to delete student")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Find students enrolled in the given course.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of students enrolled in the given course"),
            @ApiResponse(code = 500, message = "Unable to list students.")
    })
    @GetMapping("/enrollment/{courseId}")
    public ResponseEntity findStudentsByCourse(@PathVariable("courseId") int courseId) {
        final List<StudentDto> list = service.findByCourse(courseId)
                .stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok().body(list);
    }

    @ApiOperation(value = "Find students that are not subscribed in any course.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of students with no active subscription"),
            @ApiResponse(code = 500, message = "Unable to list students.")
    })
    @GetMapping("/enrollment/none")
    public ResponseEntity findNonSubscribedStudents() {
        final List<StudentDto> list = service.findNonSubscribed()
                .stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok().body(list);
    }

    private StudentDto toDto(Student obj) {
        return modelMapper.map(obj, StudentDto.class);
    }

    private Student fromDto(StudentDto dto) {
        return modelMapper.map(dto, Student.class);
    }


}

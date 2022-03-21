package io.metadata.school.registration.controller;


import io.metadata.school.registration.dto.CourseDto;
import io.metadata.school.registration.entity.Course;
import io.metadata.school.registration.service.CourseService;
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
@RequestMapping(path = "/course")
public class CourseApiRestController {

    @Autowired
    private CourseService service;

    private ModelMapper modelMapper = new ModelMapper();

    @ApiOperation(value = "Creates a new course")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Returns the created course"),
            @ApiResponse(code = 500, message = "Unable to store course")
    })
    @PostMapping
    public ResponseEntity create(@RequestBody final CourseDto dto) {
        final Course course = fromDto(dto);
        final CourseDto created = toDto(service.add(course));
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @ApiOperation(value = "Updates the specified course")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns the updated course"),
            @ApiResponse(code = 404, message = "Unable to find course with the specified id"),
            @ApiResponse(code = 500, message = "Unable to store course")
    })
    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable("id") int id, @RequestBody final CourseDto dto) {
        final Course course = fromDto(dto);
        final CourseDto created = toDto(service.update(id, course));
        return ResponseEntity.status(HttpStatus.OK).body(created);
    }

    @ApiOperation(value = "List all courses")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns the list of courses"),
            @ApiResponse(code = 500, message = "Unable to list courses")
    })
    @GetMapping
    public ResponseEntity list() {
        final List<CourseDto> courseList = service.list().stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok().body(courseList);
    }

    @ApiOperation(value = "Find course by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Returns the found course"),
            @ApiResponse(code = 404, message = "Unable to find course with the specified id"),
            @ApiResponse(code = 500, message = "Unable to find course")
    })
    @GetMapping("/{id}")
    public ResponseEntity find(@PathVariable("id") int id) {
        final CourseDto dto = toDto(service.find(id));
        return ResponseEntity.ok().body(dto);
    }

    @ApiOperation(value = "Delete course by id")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Empty body indicating deletion worked."),
            @ApiResponse(code = 404, message = "Unable to find course with the specified id"),
            @ApiResponse(code = 500, message = "Unable to delete course")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") int id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @ApiOperation(value = "Find the courses whose specified student is enrolled")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of courses whose student is enrolled"),
            @ApiResponse(code = 500, message = "Unable to list courses.")
    })
    @GetMapping("/enrollment/{studentId}")
    public ResponseEntity findStudentsByCourse(@PathVariable("studentId") int studentId) {
        final List<CourseDto> list = service.findByStudent(studentId)
                .stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok().body(list);
    }

    @ApiOperation(value = "Find courses that have no enrolled students.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of courses with no enrollment"),
            @ApiResponse(code = 500, message = "Unable to list courses.")
    })
    @GetMapping("/enrollment/none")
    public ResponseEntity findStudentsNoCourse() {
        final List<CourseDto> list = service.findNoEnrollment()
                .stream().map(this::toDto).collect(Collectors.toList());
        return ResponseEntity.ok().body(list);
    }

    private CourseDto toDto(Course obj) {
        return modelMapper.map(obj, CourseDto.class);
    }

    private Course fromDto(CourseDto dto) {
        return modelMapper.map(dto, Course.class);
    }
}

package io.metadata.school.registration.controller;


import io.metadata.school.registration.dto.EnrollmentDto;
import io.metadata.school.registration.service.EnrollmentService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/enrollment")
public class EnrollmentApiRestController {

    @Autowired
    private EnrollmentService service;

    private ModelMapper modelMapper = new ModelMapper();


    @ApiOperation(value = "Find courses that have no enrolled students.")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Empty body indicating subscription request worked."),
            @ApiResponse(code = 400, message = "Provided information is invalid (i.e.: invalid course id)."),
            @ApiResponse(code = 500, message = "Unable to store subscription request.")
    })
    @PostMapping("/subscribe")
    public ResponseEntity subscribe(@RequestBody final EnrollmentDto dto) {
        service.enroll(dto.getStudentId(), dto.getCourseId());
        return ResponseEntity.noContent().build();
    }
}

package com.baarsch_bytes.studentRegDemo.controller;

import com.baarsch_bytes.studentRegDemo.dto.CourseRequest;
import com.baarsch_bytes.studentRegDemo.model.Course;
import com.baarsch_bytes.studentRegDemo.model.Student;
import com.baarsch_bytes.studentRegDemo.repository.CourseRepository;
import com.baarsch_bytes.studentRegDemo.repository.StudentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// 1. Tell Spring to only load the CourseController into the web context
@WebMvcTest(CourseController.class)
public class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // converts Objects to JSON.
    private ObjectMapper objectMapper = new ObjectMapper();

    // 2. Inject Mockito mocks into the Spring context in place of the real repositories
    @MockitoBean
    private CourseRepository courseRepository;

    @MockitoBean
    private StudentRepository studentRepository;

    private Course mockCourse;
    private Student mockStudent;

    @BeforeEach
    void setUp() {
        // Setup some dummy entities to return from our mocked repositories
        mockStudent = new Student();
        mockStudent.setId(1L);
        mockStudent.setName("Alice");

        mockCourse = new Course();
        mockCourse.setId(100L);
        mockCourse.setName("Intro to Java");
        mockCourse.setInstructor(25L);
        mockCourse.setRoom("Room 101");
        mockCourse.setMaxSize(30);
        mockCourse.setRoster(new HashSet<>());
    }

    @Test
    void getCourses_ReturnsListOfCourseResponses() throws Exception {
        // Arrange: When the controller asks the repo for courses, return our list
        when(courseRepository.findAll()).thenReturn(List.of(mockCourse));

        // Act & Assert
        mockMvc.perform(get("/api/courses"))
                .andExpect(status().isOk())
                // Verify the DTO mapped correctly
                .andExpect(jsonPath("$[0].name").value("Intro to Java"))
                .andExpect(jsonPath("$[0].instructor").value(25L));
    }

    @Test
    void createCourse_ValidRequest_ReturnsOk() throws Exception {
        CourseRequest request = new CourseRequest();
        request.setName("Advanced Java");
        request.setInstructor(35L);
        request.setMaxSize(20);
        request.setRoom("Room 202");
        // No students initially

        // Act & Assert
        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))) // Convert our DTO to JSON
                .andExpect(status().isOk())
                .andExpect(content().string("Advanced Java added successfully"));
    }

    @Test
    void addStudent_CourseAndStudentExistAndNotFull_ReturnsOk() throws Exception {
        // Arrange
        Long courseId = 100L;
        Long studentId = 1L;

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(mockCourse));
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(mockStudent));
        // We don't need to mock save() returning anything because the controller doesn't use the return value

        // Act & Assert
        // Notice the studentId is passed in the body, as requested by your @RequestBody annotation
        mockMvc.perform(put("/api/courses/addStudent/{courseId}", courseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(studentId)))
                .andExpect(status().isOk())
                .andExpect(content().string("Alice added successfully"));
    }

    @Test
    void addStudent_CourseIsFull_ReturnsBadRequest() throws Exception {
        // Arrange
        Long courseId = 100L;
        Long studentId = 1L;

        // Force the course to be full
        mockCourse.setMaxSize(1);
        Student dummyStudent = new Student();
        dummyStudent.setName("Bob");
        mockCourse.getRoster().add(dummyStudent); // Roster size is now 1, Max is 1

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(mockCourse));
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(mockStudent));

        // Act & Assert
        mockMvc.perform(put("/api/courses/addStudent/{courseId}", courseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.valueOf(studentId)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Course is full"));
    }
}
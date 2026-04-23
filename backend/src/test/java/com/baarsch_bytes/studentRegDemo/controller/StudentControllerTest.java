package com.baarsch_bytes.studentRegDemo.controller;

import com.baarsch_bytes.studentRegDemo.dto.StudentRequest;
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

@WebMvcTest(StudentController.class)
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Manually instantiated to avoid the missing Bean error
    private ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private StudentRepository studentRepository;

    @MockitoBean
    private CourseRepository courseRepository;

    private Student mockStudent;
    private Course mockCourse;

    @BeforeEach
    void setUp() {
        mockCourse = new Course();
        mockCourse.setId(100L);
        mockCourse.setName("Data Structures");

        mockStudent = new Student();
        mockStudent.setId(1L);
        mockStudent.setName("John Doe");
        mockStudent.setMajor("Computer Science");
        mockStudent.setGpa(3.8);
        // Assuming your Student model returns an Optional<Set<Course>> based on your controller code
        mockStudent.setCourses(Set.of(mockCourse));
    }

    @Test
    void getAll_ReturnsListOfStudentResponses() throws Exception {
        when(studentRepository.findAll()).thenReturn(List.of(mockStudent));

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].major").value("Computer Science"))
                .andExpect(jsonPath("$[0].courses[0]").value("Data Structures"));
    }

    @Test
    void getStudent_ExistingId_ReturnsStudentResponse() throws Exception {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(mockStudent));

        mockMvc.perform(get("/api/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.gpa").value(3.8));
    }

    @Test
    void create_ValidRequest_ReturnsOkWithMap() throws Exception {
        StudentRequest request = new StudentRequest();
        request.setName("Jane Smith");
        request.setMajor("Mathematics");
        request.setGpa(4.0);
        request.setCourses(Set.of(100L)); // Linking to our mockCourse ID

        when(courseRepository.findAllById(request.getCourses())).thenReturn(List.of(mockCourse));

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                // Your controller returns a JSON Map with a "message" key
                .andExpect(jsonPath("$.message").value("Jane Smith added successfully"));
    }

    @Test
    void update_ValidRequest_ReturnsOkWithMap() throws Exception {
        StudentRequest request = new StudentRequest();
        request.setName("John Updated");
        request.setMajor("Physics");
        request.setGpa(3.5);

        // We must mock findById because your PUT method calls repository.findById(id).get()
        when(studentRepository.findById(1L)).thenReturn(Optional.of(mockStudent));

        mockMvc.perform(put("/api/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("John Updated updated successfully"));
    }

    @Test
    void delete_ExistingId_ReturnsOk() throws Exception {
        when(studentRepository.existsById(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/students/1"))
                .andExpect(status().isOk());
    }

    @Test
    void delete_NonExistingId_ReturnsNotFound() throws Exception {
        when(studentRepository.existsById(99L)).thenReturn(false);

        mockMvc.perform(delete("/api/students/99"))
                .andExpect(status().isNotFound());
    }
}
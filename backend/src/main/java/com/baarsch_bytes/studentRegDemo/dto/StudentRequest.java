package com.baarsch_bytes.studentRegDemo.dto;

import com.baarsch_bytes.studentRegDemo.model.Course;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

public class StudentRequest {

    @NotNull(message = "Name is required")
    @Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters")
    private String name;

    @NotNull(message = "Major is required")
    @Size(min = 1, max = 255, message = "Major must be between 1 and 255 characters")
    private String major;

    @Min(value = 0, message = "No negative GPAs allowed")
    @Max(value = 4, message = "No GPAs above 4.0 allowed")
    private Double gpa;

    private Set<Long> courses;

    public StudentRequest(){}

    public StudentRequest(String name, String major, Double gpa, Set<Long> courses) {
        this.name = name;
        this.major = major;
        this.gpa = gpa;
        this.courses = courses;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public Double getGpa() {
        return gpa;
    }

    public void setGpa(Double gpa) {
        this.gpa = gpa;
    }

    public Set<Long> getCourses() {
        return courses;
    }

    public void setCourses(Set<Long> courses) {
        this.courses = courses;
    }
}

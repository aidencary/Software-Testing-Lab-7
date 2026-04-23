package com.baarsch_bytes.studentRegDemo.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Name is required")
    @Size(min = 1, max = 255, message="Name must be between 1 and 255 characters")
    private String name;

    @NotNull(message = "Major is required")
    @Size(min = 1, max = 255, message="Major must be between 1 and 255 characters")
    private String major;

    @Min(value = 0, message = "No negative GPAs allowed")
    @Max(value = 4, message = "No GPAs above 4.0 allowed")
    private Double gpa;

    @ManyToMany(mappedBy = "roster")
    private Set<Course> courses = new HashSet<>();

    // Constructors
    public Student() {}
    public Student(String name, String major, Double gpa) {
        this.name = name;
        this.major = major;
        this.gpa = gpa;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Optional<Set<Course>> getCourses() {
        return (courses == null || courses.isEmpty())
                ? Optional.empty()
                : Optional.of(courses);
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    public void addCourse(Course course) {
        if( courses == null )
            courses = new HashSet<Course>();
        courses.add(course);
    }

}
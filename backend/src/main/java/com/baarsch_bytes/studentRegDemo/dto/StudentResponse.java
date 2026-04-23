package com.baarsch_bytes.studentRegDemo.dto;

import com.baarsch_bytes.studentRegDemo.model.Course;

import java.util.Set;

public class StudentResponse {

    private Long id;
    private String name;
    private String major;
    private Double gpa;
    private Set<String> courses;

    public StudentResponse() {
    }

    public StudentResponse(Long id, String name, String major, Double gpa, Set<String> courses) {
        this.id = id;
        this.name = name;
        this.major = major;
        this.gpa = gpa;
        this.courses = courses;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id){
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

    public Set<String> getCourses() {
        return courses;
    }

    public void setCourses(Set<String> courses) {
        this.courses = courses;
    }


}

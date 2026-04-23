package com.baarsch_bytes.studentRegDemo.dto;

import com.baarsch_bytes.studentRegDemo.model.Course;

import java.util.Set;

public class StudentRequest {

    private String name;
    private String major;
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

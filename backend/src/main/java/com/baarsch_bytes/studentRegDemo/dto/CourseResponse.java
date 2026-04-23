package com.baarsch_bytes.studentRegDemo.dto;

import java.util.Set;

public class CourseResponse {
    private Long id;
    private String name;
    private Long instructor;
    private Integer maxSize;
    private String room;
    private Set<String> roster;

    public CourseResponse(){}
    public CourseResponse(Long id, String name, Long instructor, Integer maxSize,
                          String room, Set<String> roster) {
        this.id = id;
        this.name = name;
        this.instructor = instructor;
        this.maxSize = maxSize;
        this.room = room;
        this.roster = roster;
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

    public Long getInstructor() {
        return instructor;
    }

    public void setInstructor(Long instructor) {
        this.instructor = instructor;
    }

    public Integer getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(Integer maxSize) {
        this.maxSize = maxSize;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Set<String> getRoster() {
        return roster;
    }

    public void setRoster(Set<String> roster) {
        this.roster = roster;
    }
}

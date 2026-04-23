package com.baarsch_bytes.studentRegDemo.dto;

import java.util.Set;

public class CourseRequest {
    private String name;
    private Long instructor;
    private Integer maxSize;
    private String room;
    private Set<Long> roster;

    public CourseRequest(){}
    public CourseRequest(String name, Long instructor, Integer maxSize,
                         String room, Set<Long> roster) {
        this.name = name;
        this.instructor = instructor;
        this.maxSize = maxSize;
        this.room = room;
        this.roster = roster;
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

    public Set<Long> getRoster() {
        return roster;
    }

    public void setRoster(Set<Long> roster) {
        this.roster = roster;
    }
}

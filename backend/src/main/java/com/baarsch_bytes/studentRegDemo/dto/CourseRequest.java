package com.baarsch_bytes.studentRegDemo.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;

public class CourseRequest {
    @NotNull(message = "Name is required")
    @Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters")
    private String name;

    @Min(value = 1, message = "No Instructors have negative Ids")
    private Long instructor;

    @Min(value = 1, message = "Class must allow at least one student")
    private Integer maxSize;

    @NotNull(message = "Room is required")
    @Size(min = 1, max = 255, message = "Room must be between 1 and 255 characters")
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

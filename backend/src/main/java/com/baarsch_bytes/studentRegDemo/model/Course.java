package com.baarsch_bytes.studentRegDemo.model;

import com.baarsch_bytes.studentRegDemo.validation.ValidCourseCapacity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@ValidCourseCapacity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Name is required")
    @Size(min = 1, max = 255, message="Name must be between 1 and 255 characters")
    private String name;

    @Min(value = 1, message = "No Instructors have negative Ids")
    private Long instructor;

    @Min(value = 1, message = "Class must allow at least one student")
    private Integer maxSize;

    @NotNull(message = "Room is required")
    @Size(min = 1, max = 255, message="Room must be between 1 and 255 characters")
    private String room;

    // Want to set up the Roster so that it links to Students.  But
    // I also want the students to keep track of the courses they have.
    // So, because we're in SQL-land, we'll create a join table that
    // links the course ids with the student ids.

    // A Set is used instead of a list--A Set has better performance than
    // a list in the JPA/Hibernate framework.  Additionally, a Set enforced
    // uniqueness on the java level--you don't want the same student enrolled
    // twice in the class, and you don't want a student to enroll in the same
    // class twice.

    // CascadeType refers to the way that db operations cascade to children.
    // in this case, Course is the parent and Student is the child.
    // If a Course is created (PERSIST), then students will be created along
    // with it.  If a course is Updated (MERGE) then students will also be updated
    // However we leave out CascadeType.REMOVE, because we don't want students to
    // be deleted if we delete a course.
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "course_students",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private Set<Student> roster = new HashSet<>();

    public Course() {}
    public Course(String name, Long instructor, Integer maxSize, String room, Set<Student> roster) {
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

    public
    @NotNull(message = "Name is required")
    @Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters")
    String getName() {
        return name;
    }

    public void setName(
            @NotNull(message = "Name is required")
            @Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters")
            String name) {
        this.name = name;
    }

    public
    @Min(value = 1, message = "No Instructors have negative Ids")
    Long getInstructor() {
        return instructor;
    }

    public void setInstructor(
            @Min(value = 1, message = "No Instructors have negative Ids")
            Long instructor) {
        this.instructor = instructor;
    }

    public
    @Min(value = 1, message = "Class must allow at least one student")
    Integer getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(
            @Min(value = 1, message = "Class must allow at least one student")
            Integer maxSize) {
        this.maxSize = maxSize;
    }

    public
    @NotNull(message = "Room is required")
    @Size(min = 1, max = 255, message = "Room must be between 1 and 255 characters")
    String getRoom() {
        return room;
    }

    public void setRoom(
            @NotNull(message = "Room is required")
            @Size(min = 1, max = 255, message = "Room must be between 1 and 255 characters")
            String room) {
        this.room = room;
    }

    public
    Set<Student> getRoster() {
        return roster;
    }

    public void setRoster(
            Set<Student> roster) {
        this.roster = roster;
    }
}

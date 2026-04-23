package com.baarsch_bytes.studentRegDemo.validation;

import com.baarsch_bytes.studentRegDemo.model.Course;
import com.baarsch_bytes.studentRegDemo.model.Student;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class StudentRegDemoValidationTest {

    private ConstraintValidatorContext mockContext;
    private CourseSizeValidator validator;

    @BeforeEach
    void setUp() {
        validator = new CourseSizeValidator();
        mockContext = mock(ConstraintValidatorContext.class);
    }

    /* test cases:
    course = null;
    roster = null;
    roster.size() > maxSize;
    roster.size() < maxSize;
    roster.size() == maxSize;
     */
    @Test
    void isValid_NullCourse_ReturnsFalse() {
        boolean result = validator.isValid(null, mockContext);
        assertFalse(result, "A null course should fail validation immediately.");
    }

    @Test
    public void isValid_NullRoster_returnsTrue() {
        Course mockCourse = mock (Course.class);
        when(mockCourse.getRoster()).thenReturn(null);
        when(mockCourse.getMaxSize()).thenReturn(10);

        boolean result = validator.isValid(mockCourse, mockContext);
        assertTrue(result, "A course with a null roster shoudl" +
                "be valid.");

    }
    @Test
    void isValid_RosterUnderCapacity_ReturnsTrue() {
        Course mockCourse = mock(Course.class);
        Set mockRoster = mock(Set.class);

        when(mockRoster.size()).thenReturn(15);
        when(mockCourse.getRoster()).thenReturn(mockRoster);
        when(mockCourse.getMaxSize()).thenReturn(30);

        boolean result = validator.isValid(mockCourse, mockContext);
        assertTrue(result, "15 students in a 30 person class should be valid.");
    }

    @Test
    void isValid_RosterAtExactCapacity_ReturnsTrue() {
        Course mockCourse = mock(Course.class);
        Set mockRoster = mock(Set.class);

        when(mockRoster.size()).thenReturn(30);
        when(mockCourse.getRoster()).thenReturn(mockRoster);
        when(mockCourse.getMaxSize()).thenReturn(30);

        boolean result = validator.isValid(mockCourse, mockContext);
        assertTrue(result, "30 students in a 30 person class should be valid.");
    }

    @Test
    void isValid_RosterOverCapacity_ReturnsFalse() {
        Course mockCourse = mock(Course.class);
        Set mockRoster = mock(Set.class);

        when(mockRoster.size()).thenReturn(31);
        when(mockCourse.getRoster()).thenReturn(mockRoster);
        when(mockCourse.getMaxSize()).thenReturn(30);

        boolean result = validator.isValid(mockCourse, mockContext);
        assertFalse(result, "31 students in a 30 person class should be invalid.");
    }

}

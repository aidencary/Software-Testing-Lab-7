package com.baarsch_bytes.studentRegDemo.validation;

import com.baarsch_bytes.studentRegDemo.model.Course;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CourseSizeValidator implements
        ConstraintValidator<ValidCourseCapacity, Course> {
    @Override
    public void initialize(ValidCourseCapacity constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Course course, ConstraintValidatorContext constraintValidatorContext) {
        if (course == null) return false;

        int currentSize = course.getRoster() != null ?
                course.getRoster().size() : 0;

        return currentSize <= course.getMaxSize();
    }
}

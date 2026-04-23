package com.baarsch_bytes.studentRegDemo.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CourseSizeValidator.class)
public @interface ValidCourseCapacity {
    String message() default "Number of students exceeds the maximum capacity";
    Class<?>[] groups() default{};
    Class<? extends Payload>[] payload() default {};

}

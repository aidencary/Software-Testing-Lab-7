package com.baarsch_bytes.studentRegDemo.validation;

import com.baarsch_bytes.studentRegDemo.model.Student;

public class StudentValidator {

    public boolean isValid(Student student) {

        if (student == null) {
            return false;
        }
        if (student.getName() == null || student.getName().isEmpty()) {
            return false;
        }
        if (student.getName().length() > 255) {
            return false;
        }
        if (student.getGpa() < 0 || student.getGpa() > 4.0) {
            return false;
        }
        if (student.getMajor() == null || student.getMajor().isEmpty()) {
            return false;
        }
        return student.getMajor().length() <= 255;


    }




}

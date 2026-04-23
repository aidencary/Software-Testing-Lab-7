package com.baarsch_bytes.studentRegDemo.exception;

public class NullStudentException extends Exception {
    public NullStudentException() {
        super("No such student exists.");
    }
}

package com.ljq.sms.exception;

/**
 * 学生不存在异常
 */
public class StudentNotFoundException extends RuntimeException {

    public StudentNotFoundException(String message) {
        super(message);
    }
}
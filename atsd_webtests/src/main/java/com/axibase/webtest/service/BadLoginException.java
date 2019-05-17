package com.axibase.webtest.service;

public class BadLoginException extends RuntimeException {
    public BadLoginException(String message) {
        super(message);
    }
}

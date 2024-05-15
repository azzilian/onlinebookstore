package com.onlinebookstore.onlinebookstore.exeption;

public class RegistationException extends RuntimeException {
    public RegistationException(String message, Throwable cause) {
        super(message, cause);
    }

    public RegistationException(String message) {
        super(message);
    }
}

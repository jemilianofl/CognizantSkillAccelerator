package com.adoption.exception;

public class InvalidPetDataException extends Exception {
    public InvalidPetDataException(String message) {
        super(message);
    }

    public InvalidPetDataException(String message, Throwable cause) {
        super(message, cause);
    }
}

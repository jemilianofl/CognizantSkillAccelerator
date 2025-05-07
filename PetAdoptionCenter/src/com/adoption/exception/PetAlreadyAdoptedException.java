package com.adoption.exception;

public class PetAlreadyAdoptedException extends Exception {
    public PetAlreadyAdoptedException(String message) {
        super(message);
    }
}

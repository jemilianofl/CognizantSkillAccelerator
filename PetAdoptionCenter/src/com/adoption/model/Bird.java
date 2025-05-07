package com.adoption.model;

import java.io.*;

public class Bird extends Pet implements Serializable {
    private static final long serialVersionUID = 1L;
    private double wingSpan; // in cm or inches
    private boolean canSpeak;

    public Bird(String petId, String name, int age, String breed, double wingSpan, boolean canSpeak) {
        super(petId, name, "Bird", age, breed);
        this.wingSpan = wingSpan;
        this.canSpeak = canSpeak;
    }

    // Getters and Setters
    public double getWingSpan() { return wingSpan; }
    public void setWingSpan(double wingSpan) { this.wingSpan = wingSpan; }
    public boolean canSpeak() { return canSpeak; }
    public void setCanSpeak(boolean canSpeak) { this.canSpeak = canSpeak; }

    @Override
    public String makeSound() {
        return "Chirp! Chirp!";
    }

    @Override
    public String getSpecificInfo() {
        return "Wing Span: " + wingSpan + " cm, Speaks: " + (canSpeak ? "Yes" : "No");
    }

    @Override
    public String toString() {
        return super.toString() + ", " + getSpecificInfo();
    }
}

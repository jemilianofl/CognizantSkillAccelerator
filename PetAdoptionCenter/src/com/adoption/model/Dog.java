package com.adoption.model;

import java.io.*;

public class Dog extends Pet implements Serializable {
    private static final long serialVersionUID = 1L;
    private String size; // e.g., Small, Medium, Large
    private boolean isHouseTrained;

    public Dog(String petId, String name, int age, String breed, String size, boolean isHouseTrained) {
        super(petId, name, "Dog", age, breed);
        this.size = size;
        this.isHouseTrained = isHouseTrained;
    }

    // Getters and Setters for Dog-specific attributes
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
    public boolean isHouseTrained() { return isHouseTrained; }
    public void setHouseTrained(boolean houseTrained) { isHouseTrained = houseTrained; }

    @Override
    public String makeSound() {
        return "Woof! Woof!";
    }

    @Override
    public String getSpecificInfo() {
        return "Size: " + size + ", House Trained: " + (isHouseTrained ? "Yes" : "No");
    }

    @Override
    public String toString() {
        return super.toString() + ", " + getSpecificInfo();
    }
}
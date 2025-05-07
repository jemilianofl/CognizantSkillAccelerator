package com.adoption.model;

import java.io.*;

public class Cat extends Pet implements Serializable {
    private static final long serialVersionUID = 1L;
    private String furColor;
    private boolean isDeclawed; // For informational purposes

    public Cat(String petId, String name, int age, String breed, String furColor, boolean isDeclawed) {
        super(petId, name, "Cat", age, breed);
        this.furColor = furColor;
        this.isDeclawed = isDeclawed;
    }

    // Getters and Setters
    public String getFurColor() { return furColor; }
    public void setFurColor(String furColor) { this.furColor = furColor; }
    public boolean isDeclawed() { return isDeclawed; }
    public void setDeclawed(boolean declawed) { isDeclawed = declawed; }


    @Override
    public String makeSound() {
        return "Meow!";
    }

    @Override
    public String getSpecificInfo() {
        return "Fur Color: " + furColor + ", Declawed: " + (isDeclawed ? "Yes" : "No");
    }

    @Override
    public String toString() {
        return super.toString() + ", " + getSpecificInfo();
    }
}
package com.adoption.model;

import java.io.*;
import java.util.*;

public class Adopter implements Serializable {
    private static final long serialVersionUID = 1L;

    private String adopterId;
    private String name;
    private String contactInfo; // e.g., phone number or email
    private List<Pet> adoptedPets;

    public Adopter(String adopterId, String name, String contactInfo) {
        if (adopterId == null || adopterId.trim().isEmpty()) {
            throw new IllegalArgumentException("Adopter ID cannot be null or empty.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Adopter name cannot be null or empty.");
        }
        this.adopterId = adopterId;
        this.name = name;
        this.contactInfo = contactInfo;
        this.adoptedPets = new ArrayList<>();
    }

    // Getters
    public String getAdopterId() { return adopterId; }
    public String getName() { return name; }
    public String getContactInfo() { return contactInfo; }
    public List<Pet> getAdoptedPets() { return new ArrayList<>(adoptedPets); } // Return a copy

    // Setters
    public void setName(String name) { this.name = name; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }

    public void addAdoptedPet(Pet pet) {
        if (pet != null && !this.adoptedPets.contains(pet)) {
            this.adoptedPets.add(pet);
            System.out.println(pet.getName() + " added to " + this.name + "'s adopted pets list.");
        } else if (pet != null) {
            System.out.println(pet.getName() + " is already in " + this.name + "'s adopted pets list.");
        }
    }

    public void displayAdoptedPets() {
        if (adoptedPets.isEmpty()) {
            System.out.println(name + " has not adopted any pets yet.");
        } else {
            System.out.println(name + "'s Adopted Pets:");
            for (Pet pet : adoptedPets) {
                System.out.println("- " + pet.getName() + " (" + pet.getSpecies() + ", ID: " + pet.getPetId() + ")");
            }
        }
    }

    @Override
    public String toString() {
        return "Adopter ID: " + adopterId +
                ", Name: " + name +
                ", Contact: " + contactInfo +
                ", Adopted Pets Count: " + adoptedPets.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Adopter adopter = (Adopter) o;
        return Objects.equals(adopterId, adopter.adopterId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(adopterId);
    }
}

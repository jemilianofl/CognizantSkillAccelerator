package com.adoption.model;

import com.adoption.interfaces.*;
import com.adoption.exception.PetAlreadyAdoptedException;

import java.io.*;
import java.util.*;

public abstract class Pet implements Adoptable, Vaccinable, Serializable {
    private static final long serialVersionUID = 1L; // For Serializable interface

    private String petId;
    private String name;
    private String species; // e.g., "Dog", "Cat"
    private int age; // in years
    private String breed;
    private AdoptionStatus adoptionStatus;
    private Adopter currentAdopter; // To store who adopted this pet
    private List<String> vaccinationRecord; // e.g., "Rabies - 2023-01-15"

    public Pet(String petId, String name, String species, int age, String breed) {
        if (petId == null || petId.trim().isEmpty()) {
            throw new IllegalArgumentException("Pet ID cannot be null or empty.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Pet name cannot be null or empty.");
        }
        this.petId = petId;
        this.name = name;
        this.species = species;
        this.age = age;
        this.breed = breed;
        this.adoptionStatus = AdoptionStatus.AVAILABLE;
        this.vaccinationRecord = new ArrayList<>();
        this.currentAdopter = null;
    }

    // Getters
    public String getPetId() { return petId; }
    public String getName() { return name; }
    public String getSpecies() { return species; }
    public int getAge() { return age; }
    public String getBreed() { return breed; }
    public AdoptionStatus getAdoptionStatus() { return adoptionStatus; }
    public Adopter getCurrentAdopter() { return currentAdopter; }


    // Setters
    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
    public void setBreed(String breed) { this.breed = breed; }
    // PetId is generally not changed after creation.
    // Species is also generally fixed.

    // Abstract method for specific sounds or behaviors
    public abstract String makeSound();
    public abstract String getSpecificInfo(); // To display additional info for subclasses

    // Adoptable interface implementation
    @Override
    public void markAsAdopted(Adopter adopter) throws PetAlreadyAdoptedException {
        if (this.adoptionStatus == AdoptionStatus.ADOPTED) {
            throw new PetAlreadyAdoptedException("Pet " + name + " (ID: " + petId + ") is already adopted by " + (currentAdopter != null ? currentAdopter.getName() : "someone") + ".");
        }
        this.adoptionStatus = AdoptionStatus.ADOPTED;
        this.currentAdopter = adopter;
        System.out.println(name + " has been successfully marked as adopted by " + adopter.getName() + ".");
    }

    @Override
    public boolean isAdopted() {
        return this.adoptionStatus == AdoptionStatus.ADOPTED;
    }

    @Override
    public Adopter getAdopter() {
        return this.currentAdopter;
    }

    // Vaccinable interface implementation
    @Override
    public void administerVaccine(String vaccineName, String date) {
        if (vaccineName != null && !vaccineName.trim().isEmpty() && date != null && !date.trim().isEmpty()) {
            this.vaccinationRecord.add(vaccineName + " on " + date);
            System.out.println(name + " received vaccine: " + vaccineName + " on " + date);
        } else {
            System.err.println("Invalid vaccine name or date for " + name);
        }
    }

    @Override
    public List<String> getVaccinationRecord() {
        return new ArrayList<>(vaccinationRecord); // Return a copy
    }

    @Override
    public boolean isVaccinated(String vaccineName) {
        for (String record : vaccinationRecord) {
            if (record.toLowerCase().contains(vaccineName.toLowerCase())) {
                return true;
            }
        }
        return false;
    }


    @Override
    public String toString() {
        return "Pet ID: " + petId +
                ", Name: " + name +
                ", Species: " + species +
                ", Breed: " + breed +
                ", Age: " + age +
                ", Status: " + adoptionStatus +
                (currentAdopter != null ? ", Adopted by: " + currentAdopter.getName() : "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pet pet = (Pet) o;
        return Objects.equals(petId, pet.petId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(petId);
    }
}

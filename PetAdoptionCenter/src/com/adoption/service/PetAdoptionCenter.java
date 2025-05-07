package com.adoption.service;

import com.adoption.model.*;
import com.adoption.exception.*;

import java.io.*;
import java.util.*;
import java.util.stream.*;

public class PetAdoptionCenter {
    private List<Pet> allPets; // Includes available and adopted pets
    private List<Adopter> adopters;
    private final String petsFilePath = "pets.dat"; // Store in project root or resources/
    private final String adoptersFilePath = "adopters.dat";

    public PetAdoptionCenter() {
        this.allPets = new ArrayList<>();
        this.adopters = new ArrayList<>();
        loadData(); // Load data when the center is initialized
    }

    // Pet Management
    public void addPet(Pet pet) throws InvalidPetDataException {
        if (pet == null) {
            throw new InvalidPetDataException("Cannot add a null pet.");
        }
        // Check for duplicate pet ID
        if (allPets.stream().anyMatch(p -> p.getPetId().equals(pet.getPetId()))) {
            throw new InvalidPetDataException("Pet with ID " + pet.getPetId() + " already exists.");
        }
        allPets.add(pet);
        System.out.println("Pet added: " + pet.getName() + " (ID: " + pet.getPetId() + ")");
    }

    public Optional<Pet> findPetById(String petId) {
        return allPets.stream().filter(p -> p.getPetId().equals(petId)).findFirst();
    }

    public void updatePetInformation(String petId, String newName, Integer newAge, String newBreed) throws PetNotFoundException, InvalidPetDataException {
        Pet petToUpdate = findPetById(petId)
                .orElseThrow(() -> new PetNotFoundException("Pet with ID " + petId + " not found for update."));

        if (newName != null && !newName.trim().isEmpty()) {
            petToUpdate.setName(newName);
        }
        if (newAge != null && newAge > 0) {
            petToUpdate.setAge(newAge);
        }
        if (newBreed != null && !newBreed.trim().isEmpty()) {
            petToUpdate.setBreed(newBreed);
        }
        // Add more updatable fields as needed, possibly specific to subclasses with type checking
        System.out.println("Pet information updated for ID: " + petId);
    }

    public void removePet(String petId) throws PetNotFoundException {
        Pet petToRemove = findPetById(petId)
                .orElseThrow(() -> new PetNotFoundException("Pet with ID " + petId + " not found for removal."));
        if (petToRemove.isAdopted()) {
            System.err.println("Warning: Pet " + petToRemove.getName() + " is adopted. Consider implications of removal.");
            // Optionally, remove from adopter's list as well, or prevent removal of adopted pets.
            Adopter adopter = petToRemove.getAdopter();
            if (adopter != null) {
                adopter.getAdoptedPets().remove(petToRemove); // This needs Adopter.getAdoptedPets() to return the actual list or a remove method
            }
        }
        allPets.remove(petToRemove);
        System.out.println("Pet removed: " + petToRemove.getName() + " (ID: " + petId + ")");
    }

    // Adopter Management
    public void registerAdopter(Adopter adopter) throws InvalidPetDataException {
        if (adopter == null) {
            throw new InvalidPetDataException("Cannot register a null adopter.");
        }
        if (adopters.stream().anyMatch(a -> a.getAdopterId().equals(adopter.getAdopterId()))) {
            throw new InvalidPetDataException("Adopter with ID " + adopter.getAdopterId() + " already exists.");
        }
        adopters.add(adopter);
        System.out.println("Adopter registered: " + adopter.getName() + " (ID: " + adopter.getAdopterId() + ")");
    }

    public Optional<Adopter> findAdopterById(String adopterId) {
        return adopters.stream().filter(a -> a.getAdopterId().equals(adopterId)).findFirst();
    }

    // Adoption Process
    public void processAdoption(String petId, String adopterId) throws PetNotFoundException, AdopterNotFoundException, PetAlreadyAdoptedException {
        Pet petToAdopt = findPetById(petId)
                .orElseThrow(() -> new PetNotFoundException("Pet with ID " + petId + " not found for adoption."));

        Adopter adopter = findAdopterById(adopterId)
                .orElseThrow(() -> new AdopterNotFoundException("Adopter with ID " + adopterId + " not found."));

        if (petToAdopt.isAdopted()) {
            throw new PetAlreadyAdoptedException("Pet " + petToAdopt.getName() + " is already adopted.");
        }

        petToAdopt.markAsAdopted(adopter); // This updates pet's status and currentAdopter
        adopter.addAdoptedPet(petToAdopt);
        System.out.println("Adoption successful! " + adopter.getName() + " has adopted " + petToAdopt.getName() + ".");
    }

    // Search and Filter
    public List<Pet> getAvailablePets() {
        return allPets.stream()
                .filter(pet -> !pet.isAdopted())
                .collect(Collectors.toList());
    }

    public List<Pet> searchPetsBySpecies(String species) {
        return allPets.stream()
                .filter(pet -> pet.getSpecies().equalsIgnoreCase(species) && !pet.isAdopted())
                .collect(Collectors.toList());
    }

    public List<Pet> searchPetsByAge(int age) {
        return allPets.stream()
                .filter(pet -> pet.getAge() == age && !pet.isAdopted())
                .collect(Collectors.toList());
    }
    public List<Pet> searchPetsByAgeRange(int minAge, int maxAge) {
        return allPets.stream()
                .filter(pet -> pet.getAge() >= minAge && pet.getAge() <= maxAge && !pet.isAdopted())
                .collect(Collectors.toList());
    }


    public List<Pet> searchPetsByBreed(String breed) {
        return allPets.stream()
                .filter(pet -> pet.getBreed().equalsIgnoreCase(breed) && !pet.isAdopted())
                .collect(Collectors.toList());
    }

    public List<Pet> getAllPets() {
        return new ArrayList<>(allPets); // Return a copy
    }

    public List<Adopter> getAllAdopters() {
        return new ArrayList<>(adopters); // Return a copy
    }


    // File Handling
    @SuppressWarnings("unchecked")
    public void loadData() {
        try (ObjectInputStream petsIn = new ObjectInputStream(new FileInputStream(petsFilePath));
             ObjectInputStream adoptersIn = new ObjectInputStream(new FileInputStream(adoptersFilePath))) {
            allPets = (List<Pet>) petsIn.readObject();
            adopters = (List<Adopter>) adoptersIn.readObject();
            System.out.println("Data loaded successfully from files.");
        } catch (FileNotFoundException e) {
            System.out.println("No existing data files found. Starting with an empty system.");
            allPets = new ArrayList<>(); // Ensure lists are initialized
            adopters = new ArrayList<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading data: " + e.getMessage());
            e.printStackTrace();
            allPets = new ArrayList<>(); // Initialize to empty lists in case of error
            adopters = new ArrayList<>();
        }
    }

    public void saveData() {
        try (ObjectOutputStream petsOut = new ObjectOutputStream(new FileOutputStream(petsFilePath));
             ObjectOutputStream adoptersOut = new ObjectOutputStream(new FileOutputStream(adoptersFilePath))) {
            petsOut.writeObject(allPets);
            adoptersOut.writeObject(adopters);
            System.out.println("Data saved successfully to files.");
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

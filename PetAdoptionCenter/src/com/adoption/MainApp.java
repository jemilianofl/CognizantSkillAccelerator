package com.adoption;

import com.adoption.model.*;
import com.adoption.service.PetAdoptionCenter;
import com.adoption.exception.*;

import java.util.*;

public class MainApp {
    private static PetAdoptionCenter center = new PetAdoptionCenter();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Load initial data or start fresh if files don't exist (handled by PetAdoptionCenter constructor)

        // Sample Data for initial run if files are empty
        // bootstrapInitialData(); // Call this if you want to ensure some data on first run

        boolean running = true;
        while (running) {
            printMenu();
            int choice = -1;
            try {
                if (scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                } else {
                    System.out.println("Invalid input. Please enter a number.");
                    scanner.next(); // consume the invalid input
                    continue;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Consume the invalid input
                continue;
            }
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1: addPet(); break;
                case 2: updatePet(); break;
                case 3: removePet(); break;
                case 4: registerAdopter(); break;
                case 5: adoptPet(); break;
                case 6: listAvailablePets(); break;
                case 7: listAllPets(); break;
                case 8: listAllAdopters(); break;
                case 9: searchPets(); break;
                case 10: administerVaccineToPet(); break;
                case 11: viewPetVaccinationRecord(); break;
                case 0:
                    running = false;
                    center.saveData(); // Save data before exiting
                    System.out.println("Exiting Pet Adoption Center. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
            System.out.println("------------------------------------");
        }
        scanner.close();
    }

    private static void printMenu() {
        System.out.println("\n🐾 Pet Adoption Center Menu 🐾");
        System.out.println("1. Add New Pet");
        System.out.println("2. Update Pet Information");
        System.out.println("3. Remove Pet from System");
        System.out.println("4. Register New Adopter");
        System.out.println("5. Process Adoption");
        System.out.println("6. List Available Pets");
        System.out.println("7. List All Pets (including adopted)");
        System.out.println("8. List All Adopters");
        System.out.println("9. Search Pets");
        System.out.println("10. Administer Vaccine to Pet");
        System.out.println("11. View Pet Vaccination Record");
        System.out.println("0. Save and Exit");
        System.out.print("Enter your choice: ");
    }

    private static void bootstrapInitialData() {
        // This is just for the first run if pets.dat and adopters.dat are empty
        // In a real app, you might have a more robust way or just start empty.
        if (center.getAllPets().isEmpty()) {
            try {
                center.addPet(new Dog("D001", "Buddy", 3, "Golden Retriever", "Large", true));
                center.addPet(new Cat("C001", "Whiskers", 2, "Siamese", "Cream", false));
                center.addPet(new Bird("B001", "Polly", 1, "Parrot", 25.5, true));
                center.addPet(new Dog("D002", "Lucy", 5, "Poodle", "Small", true));

                center.registerAdopter(new Adopter("A001", "Alice Smith", "alice@email.com"));
                center.registerAdopter(new Adopter("A002", "Bob Johnson", "555-1234"));
            } catch (InvalidPetDataException e) {
                System.err.println("Error bootstrapping initial data: " + e.getMessage());
            }
            System.out.println("Initial data bootstrapped (if files were empty).");
        }
    }


    private static void addPet() {
        try {
            System.out.println("\n--- Add New Pet ---");
            System.out.print("Enter Pet Type (Dog, Cat, Bird): ");
            String type = scanner.nextLine().trim();
            System.out.print("Enter Pet ID (e.g., D003): ");
            String id = scanner.nextLine().trim();
            System.out.print("Enter Name: ");
            String name = scanner.nextLine().trim();
            System.out.print("Enter Age: ");
            int age = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            System.out.print("Enter Breed: ");
            String breed = scanner.nextLine().trim();

            Pet newPet = null;
            if ("Dog".equalsIgnoreCase(type)) {
                System.out.print("Enter Size (Small, Medium, Large): ");
                String size = scanner.nextLine().trim();
                System.out.print("Is House Trained? (true/false): ");
                boolean trained = scanner.nextBoolean();
                scanner.nextLine(); // Consume newline
                newPet = new Dog(id, name, age, breed, size, trained);
            } else if ("Cat".equalsIgnoreCase(type)) {
                System.out.print("Enter Fur Color: ");
                String color = scanner.nextLine().trim();
                System.out.print("Is Declawed? (true/false): ");
                boolean declawed = scanner.nextBoolean();
                scanner.nextLine(); // Consume newline
                newPet = new Cat(id, name, age, breed, color, declawed);
            } else if ("Bird".equalsIgnoreCase(type)) {
                System.out.print("Enter Wing Span (cm): ");
                double span = scanner.nextDouble();
                scanner.nextLine(); // Consume newline
                System.out.print("Can it Speak? (true/false): ");
                boolean speaks = scanner.nextBoolean();
                scanner.nextLine(); // Consume newline
                newPet = new Bird(id, name, age, breed, span, speaks);
            } else {
                System.out.println("Invalid pet type. Pet not added.");
                return;
            }
            center.addPet(newPet);

        } catch (InputMismatchException e) {
            System.err.println("Invalid input type. Please try again.");
            scanner.nextLine(); // Consume the rest of the invalid input
        } catch (IllegalArgumentException | InvalidPetDataException e) {
            System.err.println("Error adding pet: " + e.getMessage());
        }
    }

    private static void updatePet() {
        System.out.println("\n--- Update Pet Information ---");
        System.out.print("Enter Pet ID to update: ");
        String petId = scanner.nextLine().trim();
        try {
            Optional<Pet> petOpt = center.findPetById(petId);
            if (!petOpt.isPresent()) {
                throw new PetNotFoundException("Pet with ID " + petId + " not found.");
            }
            Pet pet = petOpt.get();
            System.out.println("Updating " + pet.getName() + ". Leave blank to keep current value.");

            System.out.print("New Name (" + pet.getName() + "): ");
            String newName = scanner.nextLine().trim();
            System.out.print("New Age (" + pet.getAge() + "): ");
            String ageStr = scanner.nextLine().trim();
            Integer newAge = ageStr.isEmpty() ? null : Integer.parseInt(ageStr);
            System.out.print("New Breed (" + pet.getBreed() + "): ");
            String newBreed = scanner.nextLine().trim();

            center.updatePetInformation(petId,
                    newName.isEmpty() ? null : newName,
                    newAge,
                    newBreed.isEmpty() ? null : newBreed);

            // Add prompts for subclass-specific attributes if desired
            if (pet instanceof Dog) {
                Dog dog = (Dog) pet;
                System.out.print("New Size ("+dog.getSize()+") (Small, Medium, Large - leave blank if no change): ");
                String newSize = scanner.nextLine().trim();
                if(!newSize.isEmpty()) dog.setSize(newSize);
                System.out.print("Is House Trained? ("+dog.isHouseTrained()+") (true/false - leave blank if no change): ");
                String trainedStr = scanner.nextLine().trim();
                if(!trainedStr.isEmpty()) dog.setHouseTrained(Boolean.parseBoolean(trainedStr));
            } // Similar for Cat and Bird

        } catch (PetNotFoundException | InvalidPetDataException e) {
            System.err.println("Error updating pet: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid age format.");
        }
    }

    private static void removePet() {
        System.out.println("\n--- Remove Pet ---");
        System.out.print("Enter Pet ID to remove: ");
        String petId = scanner.nextLine().trim();
        try {
            center.removePet(petId);
        } catch (PetNotFoundException e) {
            System.err.println("Error removing pet: " + e.getMessage());
        }
    }


    private static void registerAdopter() {
        try {
            System.out.println("\n--- Register New Adopter ---");
            System.out.print("Enter Adopter ID (e.g., A003): ");
            String id = scanner.nextLine().trim();
            System.out.print("Enter Name: ");
            String name = scanner.nextLine().trim();
            System.out.print("Enter Contact Info (Phone/Email): ");
            String contact = scanner.nextLine().trim();

            Adopter newAdopter = new Adopter(id, name, contact);
            center.registerAdopter(newAdopter);

        } catch (IllegalArgumentException | InvalidPetDataException e) {
            System.err.println("Error registering adopter: " + e.getMessage());
        }
    }

    private static void adoptPet() {
        System.out.println("\n--- Process Adoption ---");
        System.out.print("Enter Pet ID to adopt: ");
        String petId = scanner.nextLine().trim();
        System.out.print("Enter Adopter ID: ");
        String adopterId = scanner.nextLine().trim();
        try {
            center.processAdoption(petId, adopterId);
        } catch (PetNotFoundException | AdopterNotFoundException | PetAlreadyAdoptedException e) {
            System.err.println("Adoption failed: " + e.getMessage());
        }
    }

    private static void listAvailablePets() {
        System.out.println("\n--- Available Pets for Adoption ---");
        List<Pet> availablePets = center.getAvailablePets();
        if (availablePets.isEmpty()) {
            System.out.println("No pets currently available for adoption.");
        } else {
            availablePets.forEach(pet -> {
                System.out.println(pet.toString() + " | " + pet.getSpecificInfo() + " | Sound: " + pet.makeSound());
                // Polymorphism in action: pet.getSpecificInfo() and pet.makeSound()
            });
        }
    }

    private static void listAllPets() {
        System.out.println("\n--- All Pets in System ---");
        List<Pet> allPets = center.getAllPets();
        if (allPets.isEmpty()) {
            System.out.println("No pets in the system.");
        } else {
            allPets.forEach(pet -> {
                System.out.println(pet.toString() + " | " + pet.getSpecificInfo());
                if (pet.isAdopted() && pet.getAdopter() != null) {
                    System.out.println("   Adopted by: " + pet.getAdopter().getName() + " (ID: " + pet.getAdopter().getAdopterId() + ")");
                }
            });
        }
    }


    private static void listAllAdopters() {
        System.out.println("\n--- Registered Adopters ---");
        List<Adopter> adopters = center.getAllAdopters();
        if (adopters.isEmpty()) {
            System.out.println("No adopters registered.");
        } else {
            adopters.forEach(adopter -> {
                System.out.println(adopter.toString());
                adopter.displayAdoptedPets(); // Shows pets adopted by this adopter
            });
        }
    }

    private static void searchPets() {
        System.out.println("\n--- Search Pets ---");
        System.out.println("Search by: 1. Species | 2. Age | 3. Breed | 4. Age Range");
        System.out.print("Enter search type: ");
        int type = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        List<Pet> results = null;

        switch (type) {
            case 1:
                System.out.print("Enter species (Dog, Cat, Bird): ");
                String species = scanner.nextLine().trim();
                results = center.searchPetsBySpecies(species);
                break;
            case 2:
                System.out.print("Enter age: ");
                int age = scanner.nextInt();
                scanner.nextLine();
                results = center.searchPetsByAge(age);
                break;
            case 3:
                System.out.print("Enter breed: ");
                String breed = scanner.nextLine().trim();
                results = center.searchPetsByBreed(breed);
                break;
            case 4:
                System.out.print("Enter minimum age: ");
                int minAge = scanner.nextInt();
                scanner.nextLine();
                System.out.print("Enter maximum age: ");
                int maxAge = scanner.nextInt();
                scanner.nextLine();
                results = center.searchPetsByAgeRange(minAge, maxAge);
                break;
            default:
                System.out.println("Invalid search type.");
                return;
        }

        if (results != null && !results.isEmpty()) {
            System.out.println("Search Results (Available for Adoption):");
            results.forEach(pet -> System.out.println(pet.toString() + " | " + pet.getSpecificInfo()));
        } else {
            System.out.println("No pets found matching your criteria or all matching pets are already adopted.");
        }
    }

    private static void administerVaccineToPet() {
        System.out.println("\n--- Administer Vaccine ---");
        System.out.print("Enter Pet ID to vaccinate: ");
        String petId = scanner.nextLine().trim();
        try {
            Pet pet = center.findPetById(petId)
                    .orElseThrow(() -> new PetNotFoundException("Pet with ID " + petId + " not found."));
            System.out.print("Enter vaccine name: ");
            String vaccineName = scanner.nextLine().trim();
            System.out.print("Enter vaccination date (YYYY-MM-DD): ");
            String vaccineDate = scanner.nextLine().trim();

            pet.administerVaccine(vaccineName, vaccineDate); // Using Vaccinable interface method
            // No need to call center.saveData() here, it will be called on exit.
            // If you want immediate persistence, you could add it.
        } catch (PetNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static void viewPetVaccinationRecord() {
        System.out.println("\n--- View Pet Vaccination Record ---");
        System.out.print("Enter Pet ID to view record: ");
        String petId = scanner.nextLine().trim();
        try {
            Pet pet = center.findPetById(petId)
                    .orElseThrow(() -> new PetNotFoundException("Pet with ID " + petId + " not found."));
            System.out.println("Vaccination Record for " + pet.getName() + " (ID: " + pet.getPetId() + "):");
            List<String> record = pet.getVaccinationRecord(); // Using Vaccinable interface method
            if (record.isEmpty()) {
                System.out.println(pet.getName() + " has no vaccination records.");
            } else {
                record.forEach(System.out::println);
            }
        } catch (PetNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}

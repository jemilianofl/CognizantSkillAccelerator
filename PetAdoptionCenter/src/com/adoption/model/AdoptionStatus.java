package com.adoption.model;

public enum AdoptionStatus {
    AVAILABLE("Available for Adoption"),
    ADOPTED("Adopted"),
    PENDING("Adoption Pending"); // Optional, can be useful

    private final String displayName;

    AdoptionStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
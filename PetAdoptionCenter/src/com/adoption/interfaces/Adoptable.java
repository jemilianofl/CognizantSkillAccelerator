package com.adoption.interfaces;

import com.adoption.model.Adopter;
import com.adoption.exception.PetAlreadyAdoptedException;

public interface Adoptable {
    void markAsAdopted(Adopter adopter) throws PetAlreadyAdoptedException;
    boolean isAdopted();
    Adopter getAdopter(); // To know who adopted the pet
}

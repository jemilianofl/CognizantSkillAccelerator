package com.adoption.interfaces;

import java.util.List;

public interface Vaccinable {
    void administerVaccine(String vaccineName, String date);
    List<String> getVaccinationRecord();
    boolean isVaccinated(String vaccineName);
}

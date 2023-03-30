package pl.lodz.p.it.ssbd2023.ssbd01.entities;

import lombok.Getter;

@Getter
public enum Category {

    ANTIBIOTICS("Antybiotyki", true),
    DIURETICS("Diuretyki", false),
    ANTIDEPRESSANTS("Antydepresanty", true);

    private String name;
    private Boolean onPrescription;

    Category(String name, boolean onPrescription) {
        this.name = name;
        this.onPrescription = onPrescription;
    }


}


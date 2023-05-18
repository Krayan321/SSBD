package pl.lodz.p.it.ssbd2023.ssbd01.entities;

import java.util.Locale;

public enum LanguageType {
    pl("pl"),
    en("en");

    String language;

    LanguageType(String language) {
        this.language = language;
    }

    public String toString() {
        return language;
    }
}

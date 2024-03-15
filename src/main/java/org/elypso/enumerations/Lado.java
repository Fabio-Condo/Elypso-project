package org.elypso.enumerations;

public enum Lado {
    FRENTE("Frente"),
    VERSO("Verso"),
    FRENTE_VERSO("Frente e Verso");

    private final String description;

    Lado(String description) {
        this.description = description;
    }

    public String getDescription() {  // if you call this, will return -> Not_started, In_progress, Done
        return description;
    }
}

package org.elypso.enumerations;

public enum Fita {

    RC_YMCKO("Color YMCKO"),
    RM_KO("Monochrome KO"),
    RM_KBLACK("Monochrome Black"),
    RM_KWHITE("Monochrome White"),
    RM_KMETALGOLD("Monochrome Metallic Gold"),
    RM_KMETALSILVER("Monochrome Metallic Silver");

    private final String description;

    Fita(String description) {
        this.description = description;
    }

    public String getDescription() {  // if you call this, will return -> Not_started, In_progress, Done
        return description;
    }
}

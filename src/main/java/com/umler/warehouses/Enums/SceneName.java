package com.umler.warehouses.Enums;

/**
 * Code created by Andrius on 2020-09-28
 */
public enum SceneName {
    DASHBOARD("DASHBOARD"),
    VISITS("CLIENT VISITS"),
    VETS ("VETERINARIANS"),
    PETS("PETS DATABASE"),
    SEARCH ("SEARCH RESULTS")
    ;

    private final String name;

    SceneName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

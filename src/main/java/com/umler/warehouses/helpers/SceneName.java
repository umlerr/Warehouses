package com.umler.warehouses.helpers;

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

    private SceneName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

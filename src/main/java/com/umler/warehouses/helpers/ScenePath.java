package com.umler.warehouses.helpers;

/**
 * Code created by Umler on 2023-04-30
 */
public enum ScenePath {

    HOME("/MainItems.fxml"),
    WAREHOUSES("/WarehousesList.fxml"),
    LOGIN("/login.fxml");

    private final String path;

    ScenePath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}

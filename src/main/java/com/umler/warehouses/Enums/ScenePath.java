package com.umler.warehouses.Enums;

/**
 * Enum для хранения путей к частям интерфейса.
 * @author Umler
 */
public enum ScenePath {

    COMPANIES("/Companies.fxml"),
    CONTRACTS("/Contracts.fxml"),
    PRODUCTS("/Products.fxml"),
    ROOMSSHELVES("/RoomsShelves.fxml"),
    ADDROOM("/addRoom.fxml"),
    ADDSHELF("/addShelf.fxml"),
    ADDCONTRACTCOMPANY("/addContractCompany.fxml"),
    ADDPRODUCT("/addProduct.fxml"),
    LOGININFO("/loginInfo.fxml"),
    LOGIN("/login.fxml");

    private final String path;

    ScenePath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}

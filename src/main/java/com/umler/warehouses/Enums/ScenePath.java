package com.umler.warehouses.Enums;

/**
 * Code created by Umler on 2023-04-30
 */
public enum ScenePath {

    COMPANIES("/Companies.fxml"),
    CONTRACTS("/Contracts.fxml"),
    ADDCONTRACTCOMPANY("/addContractCompany.fxml"),
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

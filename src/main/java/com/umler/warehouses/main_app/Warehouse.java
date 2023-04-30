package com.umler.warehouses.main_app;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Warehouse {
    private final SimpleIntegerProperty ID;
    private final SimpleStringProperty Address;

    public Warehouse(Integer ID, String Address) {
        this.ID = new SimpleIntegerProperty(ID);
        this.Address = new SimpleStringProperty(Address);
    }

    public Integer getID() {
        return ID.get();
    }

    public void setID(Integer ID) {
        this.ID.set(ID);
    }

    public String getAddress() {
        return Address.get();
    }

    public void setAddress(String Address) {
        this.Address.set(Address);
    }
}

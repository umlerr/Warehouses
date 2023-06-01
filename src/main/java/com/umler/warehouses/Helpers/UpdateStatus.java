package com.umler.warehouses.Helpers;

public final class UpdateStatus {

    private UpdateStatus() {
    }

    private static boolean isWarehouseAdded;

    public static boolean isIsWarehouseAdded() {
        return isWarehouseAdded;
    }

    public static void setIsWarehouseAdded(boolean isWarehouseAdded) {
        UpdateStatus.isWarehouseAdded = isWarehouseAdded;
    }
}

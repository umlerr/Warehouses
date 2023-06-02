package com.umler.warehouses.Helpers;

public final class UpdateStatus {

    private UpdateStatus() {
    }
    private static boolean isProductAdded;
    private static boolean isContractCompanyAdded;
    private static boolean isShelfAdded;

    public static boolean isIsContractCompanyAdded(){
        return isContractCompanyAdded;
    }

    public static void setIsContractCompanyAdded(boolean isContractCompanyAdded) {
        UpdateStatus.isContractCompanyAdded = isContractCompanyAdded;
    }

    public static boolean isIsProductAdded(){
        return isProductAdded;
    }

    public static void setIsProductAdded(boolean isProductAdded) {
        UpdateStatus.isProductAdded = isProductAdded;
    }

    public static boolean isIsShelfAdded(){
        return isShelfAdded;
    }

    public static void setIsShelfAdded(boolean isShelfAdded) {
        UpdateStatus.isShelfAdded = isShelfAdded;
    }
}

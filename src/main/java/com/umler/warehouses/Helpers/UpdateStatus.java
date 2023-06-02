package com.umler.warehouses.Helpers;

public final class UpdateStatus {

    private UpdateStatus() {
    }

    private static boolean isContractCompanyAdded;

    public static boolean isIsContractCompanyAdded(){
        return isContractCompanyAdded;
    }

    public static void setIsContractCompanyAdded(boolean isContractCompanyAdded) {
        UpdateStatus.isContractCompanyAdded = isContractCompanyAdded;
    }
}

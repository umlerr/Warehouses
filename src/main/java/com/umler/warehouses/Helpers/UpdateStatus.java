package com.umler.warehouses.Helpers;


/**
 * Класс для проверки на добавление элемента в базу данных
 * @author Umler
 */

public final class UpdateStatus {

    private UpdateStatus() {
    }
    private static boolean isProductAdded;
    private static boolean isContractCompanyAdded;
    private static boolean isShelfAdded;
    private static boolean isRoomAdded;

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

    public static boolean isIsRoomAdded(){
        return isRoomAdded;
    }

    public static void setIsRoomAdded(boolean isRoomAdded) {
        UpdateStatus.isRoomAdded = isRoomAdded;
    }
}

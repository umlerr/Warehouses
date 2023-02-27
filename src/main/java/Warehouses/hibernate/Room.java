package Warehouses.hibernate;

import java.util.ArrayList;

public class Room {
    private String RoomNumber;
    private ArrayList<Shelf> Shelfs;
    public String GetInfo() {
        /**
         * @return Информация о помещении
         */
        return("1");
    }

    /**
     * @param Info Информация о помещении
     */
    public void SetInfo(String Info) {

    }
    public String GetNumberOfShelfs(){
        /**
         * @return Кол-во стеллажей
         */
        return("1");
    }
    public String GetFullnessOfShelfs(){
        /**
         * @return Заполненность стеллажей
         */
        return("1");
    }
    public static void main(String[] args) {

    }
}

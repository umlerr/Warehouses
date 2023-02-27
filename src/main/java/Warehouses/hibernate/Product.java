package Warehouses.hibernate;

import java.util.ArrayList;

public class Product {
    private String Name; //Название груза
    private Integer Capacity; //Тип груза
    private String ID; //Уникальный идентификатор
    private ArrayList<Company> Company; //Компания-Владелец
    public String GetInfo() {
        /**
         * @return Информация о грузе
         */
        return("1");
    }

    /**
     * @param Info Информация о грузе
     */
    public void SetInfo(String Info) {

    }
    public static void main(String[] args) {

    }
}

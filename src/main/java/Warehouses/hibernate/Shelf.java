package Warehouses.hibernate;

import java.util.ArrayList;

public class Shelf {
    private Integer Capacity; //Вместимость стеллажа
    private ArrayList<Product> Products; //Грузы
    public String GetInfo() {
        /**
         * @return Информация о стеллаже
         */
        return("1");
    }

    /**
     * @param Info Информация о стеллаже
     */
    public void SetInfo(String Info) {

    }
    /**
     * @param Capacity Вместимость стеллажа
     */
    public void SetCapacity(Integer Capacity){

    }
    public String GetFullnessOfShelf(){
        /**
         * @return Заполненность стеллажей
         */
        return("1");
    }
    public String GetFullness(){
        /**
         * @return Заполненность стеллажа
         */
        return("1");
    }
    public String GetProductList(){
        /**
         * @return Заполненность стеллажей
         */
        return("1");
    }
    public static void main(String[] args) {

    }
}

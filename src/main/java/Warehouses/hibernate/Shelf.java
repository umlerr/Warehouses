package Warehouses.hibernate;

import java.util.ArrayList;

public class Shelf {
    private int ShelfID;
    private Integer Capacity; //Вместимость стеллажа
    private ArrayList<Product> Products; //Грузы
    public String GetInfo() {
        return("1");
    }

    public void SetInfo(String Info) {

    }

    public void SetCapacity(Integer Capacity){

    }
    public String GetFullnessOfShelf(){

        return("1");
    }
    public String GetFullness(){

        return("1");
    }
    public String GetProductList(){

        return("1");
    }
    public static void main(String[] args) {

    }
}

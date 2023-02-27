package Warehouses.hibernate;

import java.util.ArrayList;

public class Shelf {

    private int ShelfID;
    private float Capacity;
    private ArrayList<Product> Products;

    public int GetShelfID(){
        return ShelfID;
    }
    public void SetShelfID(int ShelfID){
        this.ShelfID = ShelfID;
    }

    public float GetShelfCapacity(){
        return Capacity;
    }
    public void SetShelfCapacity(float ShelfCapacity){
        this.Capacity = ShelfCapacity;
    }

    public ArrayList<Product> GetListOfProducts(){
        return Products;
    }

    public static void main(String[] args) {

    }
}

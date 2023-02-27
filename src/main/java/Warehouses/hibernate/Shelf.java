package Warehouses.hibernate;

import java.util.List;

public class Shelf {

    private int ShelfID;
    private float Capacity;
    private List<Product> Products;

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

    public List<Product> GetListOfProducts(){
        return Products;
    }

    public static void main(String[] args) {

    }
}

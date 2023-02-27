package Warehouses.hibernate;

import java.util.ArrayList;

public class Product {
    private int ProductID;
    private String Name;
    private String Type;
    private ArrayList<Company> Companies;

    public int GetProductID(){
        return ProductID;
    }
    public void SetProductID(int ProductID){
        this.ProductID = ProductID;
    }

    public String GetProductName(){
        return Name;
    }
    public void SetProductName(String ProductName){
        this.Name = ProductName;
    }

    public String GetProductType(){
        return Type;
    }
    public void SetProductType(String ProductType){
        this.Type = ProductType;
    }

    public ArrayList<Company> GetListOfCompanies(){
        return Companies;
    }

    public static void main(String[] args) {

    }
}

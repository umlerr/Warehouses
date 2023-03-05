package Warehouses.hibernate;

import javax.persistence.*;
import java.util.ArrayList;

@Entity
@Table(name="Products")
public class Product {
    @Id
    @Column(name = "ProductID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ProductID;
    @Column(name = "Name")
    private String Name;
    @Column(name = "Type")
    private String Type;
    @OneToOne
    @JoinColumn(name = "CompanyId")
    private Company CompanyInfo;
    @ManyToOne
    @JoinColumn(name = "ShelfId")
    private Shelf ShelfId;
    public Shelf getShelfId(){
        return ShelfId;
    }
    public void setShelfId(Shelf ShelfId){
        this.ShelfId = ShelfId;
    }

    public Company getCompanyInfo(){
        return this.CompanyInfo;
    }
    public void setCompanyInfo(Company CompanyInfo){
        this.CompanyInfo = CompanyInfo;
    }
    public int getProductID(){
        return ProductID;
    }
    public void setProductID(int ProductID){
        this.ProductID = ProductID;
    }

    public String getName(){
        return Name;
    }
    public void setName(String ProductName){
        this.Name = ProductName;
    }

    public String getType(){
        return Type;
    }
    public void setType(String ProductType){
        this.Type = ProductType;
    }

    public static void main(String[] args) {

    }
}

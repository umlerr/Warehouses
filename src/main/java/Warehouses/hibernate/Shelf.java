package Warehouses.hibernate;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name="Shelfs")
public class Shelf {
    @Id
    @Column(name = "ShelfID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ShelfID;
    @Column(name = "Capacity")
    private float Capacity;
    @OneToMany(mappedBy = "Products")
    private List<Product> Products;
    @ManyToOne
    @JoinColumn(name = "RoomId")
    private Room RoomId;
    public Room getRoomId(){
        return RoomId;
    }
    public void setRoomId(Room RoomId){
        this.RoomId = RoomId;
    }
    public int getShelfID(){
        return ShelfID;
    }
    public void setShelfID(int ShelfID){
        this.ShelfID = ShelfID;
    }
    public float getCapacity(){
        return Capacity;
    }
    public void setCapacity(float ShelfCapacity) {
        this.Capacity = ShelfCapacity;
    }
    public List<Product> getProducts(){
        return Products;
    }
    public void setProducts(List<Product> Products){
       this.Products = Products;
    }
    public static void main(String[] args) {

    }
}

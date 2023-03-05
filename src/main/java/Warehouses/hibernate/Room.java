package Warehouses.hibernate;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="Rooms")
public class Room {
    @Id
    @Column(name = "RoomID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int RoomID;
    @OneToMany(mappedBy = "Shelfs")
    private List<Shelf> Shelfs;
    @ManyToOne
    @JoinColumn(name = "WarehouseID")
    private Warehouse WarehouseID;
    public Warehouse getWarehouseID(){
        return this.WarehouseID;
    }
    public void setWarehouseID(Warehouse WarehouseID){
        this.WarehouseID = WarehouseID;
    }
    public int getRoomID(){
        return RoomID;
    }
    public void setRoomID(int RoomID){
        this.RoomID = RoomID;
    }
    public List<Shelf> getShelfs(){
        return Shelfs;
    }
    public void setShelfs(List<Shelf> Shelfs){
        this.Shelfs = Shelfs;
    }

    public String NumberOfShelfs(){
        return("Number of Shelfs: ");
    }

    public String FullnessOfShelfs(){
        return("Fullness of shelfs: ");
    }

    public static void main(String[] args) {

    }
}

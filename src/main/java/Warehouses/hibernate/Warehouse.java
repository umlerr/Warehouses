package Warehouses.hibernate;

import javax.persistence.*;
import java.lang.String;
import java.util.*;

@Entity
@Table(name="Warehouses")
public class Warehouse {
    @Id
    @Column(name = "WarehouseID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int WarehouseID;
    @Column(name = "Address")
    private String Address;
    @OneToMany(mappedBy = "Contracts")
    private List<Contract> Contracts;
    @OneToMany(mappedBy = "Managers")
    private List<Manager> Managers;
    @OneToMany(mappedBy = "Rooms")
    private List<Room> Rooms;
    public int getWarehouseID(){
        return WarehouseID;
    }
    public void setWarehouseID(int WarehouseID){
        this.WarehouseID = WarehouseID;
    }
    public String getAddress(){
        return Address;
    }
    public void setAddress(String Address){
        this.Address = Address;
    }
    public List<Contract> getContracts(){
        return this.Contracts;
    }
    public void setContracts(List<Contract> Contracts){
        this.Contracts = Contracts;
    }

    public List<Manager> getManagers(){
        return Managers;
    }
    public void setManagers(List<Manager> Managers){
        this.Managers = Managers;
    }
    public List<Room> getRooms(){
        return Rooms;
    }
    public void setRooms(List<Room> Rooms){
        this.Rooms = Rooms;
    }
    public String Fullness(){
        return("Fullness:");
    }
    public String TypeNumber(){
        return("Type:");
    }
    public static void main(String[] args) {

    }
}

package Warehouses.hibernate;

import javax.persistence.*;
import java.lang.String;
import java.util.*;

@Entity
@Table(name="Warehouses.Warehouses")
public class Warehouse {
    private int WarehouseID;
    private String Address;
    private List<Contract> Contracts;
    private List<Manager> Managers;
    private List<Room> Rooms;
    @Id
    @Column(name = "WarehouseID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getWarehouseID(){
        return WarehouseID;
    }
    public void setWarehouseID(int WarehouseID){
        this.WarehouseID = WarehouseID;
    }
    @Column(name = "Address")
    public String getAddress(){
        return Address;
    }
    public void setAddress(String Address){
        this.Address = Address;
    }
    public List<Contract> GetContractList(){
        return Contracts;
    }
    public List<Manager> GetManagersList(){
        return Managers;
    }
    public List<Room> GetRoomsList(){
        return Rooms;
    }
    public String GetFullness(){
        return("Fullness:");
    }
    public String GetTypeNumber(){
        return("Type:");
    }
    public static void main(String[] args) {

    }
}

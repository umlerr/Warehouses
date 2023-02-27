package Warehouses.hibernate;

import javax.persistence.*;
import java.lang.String;
import java.util.*;

@Entity
@Table(name="Warehouses")
public class Warehouse {
    private int WarehouseID;
    private String Address;
    private ArrayList<Contract> Contracts;
    private ArrayList<Manager> Managers;
    private ArrayList<Room> Rooms;
    @Id
    @Column(name = "WarehouseID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int GetWarehouseID(){
        return WarehouseID;
    }
    public void SetWarehouseID(int WarehouseID){
        this.WarehouseID = WarehouseID;
    }
    @Column(name = "Address")
    public String GetAddress(){
        return Address;
    }
    public void SetAddress(String Address){
        this.Address = Address;
    }
    public ArrayList<Contract> GetContractList(){
        return Contracts;
    }
    public ArrayList<Manager> GetManagersList(){
        return Managers;
    }
    public ArrayList<Room> GetRoomsList(){
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

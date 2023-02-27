package Warehouses.hibernate;

import java.util.ArrayList;

public class Room {
    private int RoomID;
    private ArrayList<Shelf> Shelfs;

    public int RoomID(){
        return RoomID;
    }
    public void RoomID(int RoomID){
        this.RoomID = RoomID;
    }
    public ArrayList<Shelf> GetListOfShelfs(){
        return Shelfs;
    }
    public String GetNumberOfShelfs(){
        return("Number of Shelfs: ");
    }
    public String GetFullnessOfShelfs(){
        return("Fullness of shelfs: ");
    }
    public static void main(String[] args) {

    }
}

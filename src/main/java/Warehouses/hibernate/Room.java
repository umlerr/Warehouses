package Warehouses.hibernate;

import java.util.List;

public class Room {
    private int RoomID;
    private List<Shelf> Shelfs;

    public int GetRoomID(){
        return RoomID;
    }
    public void SetRoomID(int RoomID){
        this.RoomID = RoomID;
    }

    public List<Shelf> GetListOfShelfs(){
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

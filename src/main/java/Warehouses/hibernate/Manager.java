package Warehouses.hibernate;

public class Manager {
    private int ManagerID;
    private String Name;
    private String Surname;

    public int GetManagerID(){
        return ManagerID;
    }
    public void SetManagerID(int ManagerID){
        this.ManagerID = ManagerID;
    }

    public String GetManagerName(){
        return Name;
    }
    public void SetManagerName(String ManagerName){
        this.Name = ManagerName;
    }

    public String GetManagerSurname(){
        return Surname;
    }
    public void SetManagerSurname(String ManagerSurname){
        this.Surname = ManagerSurname;
    }

    public static void main(String[] args) {

    }
}

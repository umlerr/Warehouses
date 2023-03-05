package Warehouses.hibernate;

import javax.persistence.*;

@Entity
@Table(name="Managers")
public class Manager {
    @Id
    @Column(name = "ManagerID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ManagerID;
    @Column(name = "Name")
    private String Name;
    @Column(name = "Surname")
    private String Surname;
    @ManyToOne
    @JoinColumn(name = "WarehouseID")
    private Warehouse WarehouseID;
    public Warehouse getWarehouseID(){
        return this.WarehouseID;
    }
    public void setWarehouseID(Warehouse WarehouseID){
        this.WarehouseID = WarehouseID;
    }
    public int getManagerID(){
        return ManagerID;
    }
    public void setManagerID(int ManagerID){
        this.ManagerID = ManagerID;
    }
    public String getName(){
        return Name;
    }
    public void setName(String ManagerName){
        this.Name = ManagerName;
    }
    public String getSurname(){
        return Surname;
    }
    public void setSurname(String ManagerSurname){
        this.Surname = ManagerSurname;
    }
    public static void main(String[] args) {

    }
}

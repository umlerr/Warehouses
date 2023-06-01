package com.umler.warehouses.Model;


import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "warehouse")
public class Warehouse {

    @Id
    @Column(name = "id_warehouse")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_warehouse;
    @Column(name = "address")
    private String address;

    @OneToMany(mappedBy = "warehouse", cascade=CascadeType.ALL, orphanRemoval=true)
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    private Set<Manager> managerList = new HashSet<>();

    @OneToMany(mappedBy = "warehouse", cascade=CascadeType.ALL, orphanRemoval=true)
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    private Set<Room> roomList;
    @OneToMany(mappedBy = "warehouse", cascade=CascadeType.ALL, orphanRemoval=true)
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    private Set<Contract> contractList;

    public Set<Contract> getContractList() {
        return contractList;
    }

    public void setContractList(Set<Contract> contractList) {
        this.contractList = contractList;
    }

    public Set<Room> getRoomList() {
        return roomList;
    }

    public void setRoomList(Set<Room> roomList) {
        this.roomList = roomList;
    }

    public Set<Manager> getManagerList() {
        return managerList;
    }

    public void setManagerList(Set<Manager> managerList) {
        this.managerList = managerList;
    }

    public void addManager(Manager manager) {
        managerList.add(manager);
        manager.setWarehouse(this);
    }
    public void removeManager(Manager manager) {
        managerList.remove(manager);
        manager.setWarehouse(null);
    }
    public Warehouse() {
    }

    public Integer getId_warehouse() {
        return id_warehouse;
    }

    public void setId_warehouse(Integer id_warehouse) {
        this.id_warehouse = id_warehouse;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}

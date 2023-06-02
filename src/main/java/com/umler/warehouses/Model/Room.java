package com.umler.warehouses.Model;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Room")
public class Room {
    @Id
    @Column(name = "id_room")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_room;
    @Column(name = "number")
    private Integer number;
    @Column(name = "capacity")
    private Integer capacity;

    @OneToMany(mappedBy = "room", cascade=CascadeType.ALL, orphanRemoval=true)
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    private List<Shelf> shelvesList = new ArrayList<>();

    public List<Shelf> getShelvesList() {
        return shelvesList;
    }

    public void setShelvesList(List<Shelf> shelvesList) {
        this.shelvesList = shelvesList;
    }

    public Integer getId_room() {
        return id_room;
    }

    public void setId_room(Integer id_room) {
        this.id_room = id_room;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return String.format("%s", this.number);
    }
}

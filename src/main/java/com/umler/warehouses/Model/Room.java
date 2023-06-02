package com.umler.warehouses.Model;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="Room")
public class Room {
    @Id
    @Column(name = "id_room")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_room;
    @Column(name = "number")
    private Integer Number;

    @OneToMany(mappedBy = "room", cascade=CascadeType.ALL, orphanRemoval=true)
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    private Set<Shelf> shelfList;

    public Set<Shelf> getShelfList() {
        return shelfList;
    }

    public void setShelfList(Set<Shelf> shelfList) {
        this.shelfList = shelfList;
    }


    public Integer getId_room() {
        return id_room;
    }

    public void setId_room(Integer id_room) {
        this.id_room = id_room;
    }

    public Integer getNumber() {
        return Number;
    }

    public void setNumber(Integer number) {
        Number = number;
    }
}

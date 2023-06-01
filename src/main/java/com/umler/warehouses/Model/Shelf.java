package com.umler.warehouses.Model;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="Shelf")
public class Shelf {
    @Id
    @Column(name = "id_shelf")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_shelf;
    @Column(name = "number")
    private Integer Number;
    @Column(name = "capacity")
    private Integer Capacity;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    private Room room;

    @OneToMany(mappedBy = "shelf", cascade=CascadeType.ALL, orphanRemoval=true)
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    private Set<Product> productList;

    public Set<Product> getProductList() {
        return productList;
    }

    public void setProductList(Set<Product> productList) {
        this.productList = productList;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Integer getId_shelf() {
        return id_shelf;
    }

    public void setId_shelf(Integer id_shelf) {
        this.id_shelf = id_shelf;
    }

    public Integer getNumber() {
        return Number;
    }

    public void setNumber(Integer number) {
        Number = number;
    }

    public Integer getCapacity() {
        return Capacity;
    }

    public void setCapacity(Integer capacity) {
        Capacity = capacity;
    }
}

package com.umler.warehouses.Model;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс стеллажа
 * @author Umler
 */

@Entity
@Table(name="Shelf")
public class Shelf {
    @Id
    @Column(name = "id_shelf")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_shelf;
    @Column(name = "number")
    private Integer number;
    @Column(name = "capacity")
    private Integer capacity;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @OneToMany(mappedBy = "shelf", cascade=CascadeType.ALL, orphanRemoval=true, fetch = FetchType.EAGER)
    @Cascade(org.hibernate.annotations.CascadeType.DELETE)
    private List<Product> productList = new ArrayList<>();

    public List<Product> getProductList() {
        return productList;
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

    /**
     * Переопределение метода toString для корректного вывода объектов класса.
     */
    @Override
    public String toString() {
        return String.format("%s", this.number);
    }
}

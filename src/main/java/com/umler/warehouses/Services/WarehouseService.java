package com.umler.warehouses.Services;

import com.umler.warehouses.Model.Product;
import com.umler.warehouses.Model.Room;
import com.umler.warehouses.Model.Shelf;

import java.util.List;

/**
 * Класс для подсчета обзей заполненности склада
 * @author Umler
 */

public class WarehouseService {

    RoomService roomService = new RoomService();

    /**
     * Возвращает заполенность склада.
     * @return заполенность склада
     */
    public Integer getFullnessOfWarehouse()
    {
        try
        {
            int roomcapacity = 0;
            List<Room> rooms = roomService.getRooms();
            if(rooms.size() != 0)
            {
                for (Room room : rooms){
                    List<Shelf> shelves = room.getShelvesList();
                    int shelfcapacity = 0;
                    if(shelves.size()!=0)
                    {
                        for (Shelf shelf : shelves) {
                            List<Product> products = shelf.getProductList();
                            Integer product_space = 0;
                            for (Product product : products) {
                                product_space += product.getQuantity();
                            }
                            shelfcapacity += product_space * 100 / shelf.getCapacity();
                        }
                        roomcapacity += shelfcapacity / shelves.size();
                    }
                }
                return roomcapacity / rooms.size();
            }
        }
        catch (NullPointerException ex)
        {
            System.out.println("NULL" + ex);
        }
        return null;
    }
}
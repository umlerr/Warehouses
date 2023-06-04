package com.umler.warehouses.Services;


import com.umler.warehouses.Helpers.HibernateUtil;
import com.umler.warehouses.Model.Company;
import com.umler.warehouses.Model.Product;
import com.umler.warehouses.Model.Shelf;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import java.util.ArrayList;
import java.util.List;


public class WarehouseService {

    ShelfService shelfService = new ShelfService();

    ProductService productService = new ProductService();

    public Integer getFullnessOfWarehouse()
    {
        try
        {
            Integer product_space = 0;
            Integer warehouseCapacity = 0;
            List<Shelf> shelves = shelfService.getShelves();
            List<Product> products = productService.getProducts();

            for (Shelf shelf : shelves) {
                warehouseCapacity += shelf.getCapacity();
            }

            for (Product product : products) {
                product_space += product.getQuantity();
            }

            return product_space*100/warehouseCapacity;
        }
        catch (NullPointerException ex)
        {
            System.out.println("NULL" + ex);
        }
        return null;
    }
}
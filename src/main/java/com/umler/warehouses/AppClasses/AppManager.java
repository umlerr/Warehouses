package com.umler.warehouses.AppClasses;

import com.umler.warehouses.Helpers.HibernateUtil;
import com.umler.warehouses.Model.Manager;
import com.umler.warehouses.Model.Warehouse;
import org.hibernate.Session;
import org.hibernate.Transaction;


public class AppManager {
    public static void main(String[] args){

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        Warehouse warehouse = new Warehouse();
        warehouse.setAddress("ул. Профессора Попова, дом 5 литера Ф, Санкт-Петербург, 197022");

        Manager manager = new Manager();
        manager.setName("David");
        manager.setSurname("Airapetov");

        Manager manager2 = new Manager();
        manager2.setName("Andrey");
        manager2.setSurname("Vinogradov");

        manager.setWarehouse(warehouse);
        manager2.setWarehouse(warehouse);

        session.save(warehouse);
        session.save(manager);
        session.save(manager2);

        transaction.commit();
        System.exit(0);
    }

}

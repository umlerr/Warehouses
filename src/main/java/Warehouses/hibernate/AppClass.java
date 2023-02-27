package Warehouses.hibernate;

import javax.persistence.*;
import java.lang.String;
import java.util.*;

public class AppClass {
    public static void main(String[] args){
        EntityManagerFactory EMF = Persistence.createEntityManagerFactory("Persistence-Warehouses");
        EntityManager EM = EMF.createEntityManager();

        EM.getTransaction().begin();

        Warehouse ST = new Warehouse();
        ST.SetAddress("St.Petersburg ul. Professora Popova 5, 197022 St. Petersburg, Russian Federation");
        EM.persist(ST);

        EM.getTransaction().commit();

        System.out.println("New station ID is" + ST.GetWarehouseID());
    }
}

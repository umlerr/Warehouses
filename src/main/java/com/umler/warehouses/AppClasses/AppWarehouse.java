package com.umler.warehouses.AppClasses;

import com.umler.warehouses.Model.*;
import com.umler.warehouses.Helpers.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;


public class AppWarehouse {
    public static void main(String[] args){

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        Warehouse warehouse = new Warehouse();
        warehouse.setAddress("ул. Профессора Попова, дом 5 литера Ф, Санкт-Петербург, 197022");

        Room room = new Room();
        room.setNumber(1);
        room.setWarehouse(warehouse);

        Contract contract = new Contract();
        contract.setStartDate(LocalDate.of(11,12,1));
        contract.setEndDate(LocalDate.of(1111,12,1));
        contract.setWarehouse(warehouse);

        Shelf shelf = new Shelf();
        shelf.setNumber(1);
        shelf.setCapacity(200);
        shelf.setRoom(room);


        Company company = new Company();

        company.setAddress("Professora popova 2");
        company.setName("Morevolnuetsa.da");
        company.setPhoneNumber("1234567891");
        company.setMSRN("123456789123456");
        company.setTIN("123456789123");
        company.setContract(contract);


        Product product = new Product();
        product.setName("Двустороний фалос с моторчиком");
        product.setType("Вибратор");
        product.setShelf(shelf);
        product.setCompany(company);

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
        session.save(contract);
        session.save(company);
        session.save(room);
        session.save(shelf);
        session.save(product);

        transaction.commit();
    }

}

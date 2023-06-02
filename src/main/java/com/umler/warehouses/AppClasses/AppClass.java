package com.umler.warehouses.AppClasses;

import com.umler.warehouses.Helpers.HibernateUtil;
import com.umler.warehouses.Model.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;


public class AppClass {
    public static void main(String[] args){

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();

        Contract contract = new Contract();
        contract.setStartDate(LocalDate.of(11,12,1));
        contract.setEndDate(LocalDate.of(1111,12,1));


        Company company = new Company();

        company.setAddress("Professora popova 2");
        company.setName("Morevolnuetsa.da");
        company.setPhoneNumber("1234567891");
        company.setTIN("123456789123");


        Room room = new Room();
        room.setNumber(1);

        Shelf shelf = new Shelf();
        shelf.setNumber(1);
        shelf.setCapacity(200);
        shelf.setRoom(room);

        Product product = new Product();
        product.setName("Двустороний фалос с моторчиком");
        product.setType("Вибратор");
        product.setShelf(shelf);
        product.setCompany(company);


        contract.setCompany(company);
        shelf.setRoom(room);
        product.setShelf(shelf);
        product.setCompany(company);


        session.save(company);
        session.save(contract);
        session.save(room);
        session.save(shelf);
        session.save(product);

        transaction.commit();
        System.exit(0);
    }

}

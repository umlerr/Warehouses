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
        contract.setStartdate(LocalDate.of(2015,12,1));
        contract.setEnddate(LocalDate.of(2025,12,1));
        contract.setNumber(231);

        Company company = new Company();

        company.setAddress("St.Petersburg, " + "st.Popova, " + "5, " + "194000");
        company.setName("LETI");
        company.setPhoneNumber("9959895421");
        company.setTin("1234567891");

        Room room = new Room();
        room.setNumber(1);
        room.setCapacity(100);

        Shelf shelf = new Shelf();
        shelf.setNumber(1);
        shelf.setCapacity(200);
        shelf.setRoom(room);

        Product product = new Product();
        product.setName("DDDD");
        product.setType("dsadasddas");

        product.setShelf(shelf);
        product.setContract(contract);

        contract.setCompany(company);
        shelf.setRoom(room);


        session.save(company);
        session.save(contract);
        session.save(room);
        session.save(shelf);
        session.save(product);

        transaction.commit();
        System.exit(0);
    }

}

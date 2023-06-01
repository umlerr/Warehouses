package com.umler.warehouses.AppClasses;

import com.umler.warehouses.Helpers.HibernateUtil;
import com.umler.warehouses.Model.Contract;
import com.umler.warehouses.Model.Company;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDate;


public class AppContract {
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
        company.setMSRN("123456789123456");
        company.setTIN("123456789123");

        company.setContract(contract);

        session.save(contract);
        session.save(company);

        transaction.commit();
        System.exit(0);
    }

}

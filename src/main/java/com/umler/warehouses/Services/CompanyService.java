package com.umler.warehouses.Services;


import com.umler.warehouses.Helpers.HibernateUtil;
import com.umler.warehouses.Model.Company;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import java.util.ArrayList;
import java.util.List;


/**
 * Класс для работы с таблицей Company в базе данных
 * @author Umler
 */

public class CompanyService {

    /**
     * Создает новой компании или обновляет существующую в базе данных.
     * @param company объект, представляющий компанию
     * @return true, если операция выполнена успешно, false в противном случае
     */
    public Boolean createCompany(Company company) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(company);
            transaction.commit();
            return transaction.getStatus() == TransactionStatus.COMMITTED;
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * Обновляет информацию о компании в базе данных.
     * @param company объект, представляющий компанию с обновленными данными
     */
    public void updateCompany(Company company) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(company);
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
    }

    /**
     * Удаляет информацию о компании из базы данных.
     * @param company объект, представляющий компанию, которую нужно удалить
     */
    public void deleteCompany(Company company) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(company);
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
    }

    /**
     * Возвращает список всех компаний из базы данных.
     * @return список объектов, представляющих компаний
     */
    public List<Company> getCompanies() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Company> companies = session.createQuery("from Company", Company.class).list();
            for(Company company : companies){
                Hibernate.initialize(company.getContractList());
            }
            return companies;
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

}
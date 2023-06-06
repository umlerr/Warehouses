package com.umler.warehouses.Services;

import com.umler.warehouses.Helpers.HibernateUtil;
import com.umler.warehouses.Model.Room;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Класс для работы с таблицей Room в базе данных
 * @author Umler
 */

public class RoomService {

    /**
     * Создает новое помещение или обновляет существующую в базе данных.
     * @param room объект, представляющий помещение
     * @return true, если операция выполнена успешно, false в противном случае
     */
    public Boolean createRoom(Room room) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.saveOrUpdate(room);
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
     * Обновляет информацию о помещении в базе данных.
     * @param room объект, представляющий помещение с обновленными данными
     */
    public void updateRoom(Room room) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(room);
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
    }

    /**
     * Удаляет информацию о помещении из базы данных.
     * @param room объект, представляющий помещении, который нужно удалить
     */
    public void deleteRoom(Room room) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(room);
            transaction.commit();
        } catch (Exception ex) {
            if (transaction != null) {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
    }

    /**
     * Возвращает помещение по номеру.
     * @param number номер помещения для поиска в БД.
     * @return помещение по номеру
     */
    public Room getRoom(Integer number) {
        List<Room> t = getRooms();
        for (Room room : t){
            if (Objects.equals(room.getNumber(), number))
                return room;
        }
        return null;
    }

    /**
     * Возвращает список всех помещений из базы данных.
     * @return список объектов, представляющих помещения
     */
    public List<Room> getRooms() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Room ", Room.class).list();
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }
}

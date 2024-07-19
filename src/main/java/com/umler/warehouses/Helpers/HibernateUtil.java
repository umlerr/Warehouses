package com.umler.warehouses.Helpers;

import com.umler.warehouses.Model.*;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;


/**
 * Класс HibernateUtil предоставляет методы для работы с Hibernate.
 * Он содержит методы для создания объекта SessionFactory и закрытия его.
 * @author Umler
 */

public class HibernateUtil {

    private static SessionFactory sessionFactory;

    private static final Logger logger = LoggerFactory.getLogger("Hibernate Logger");

    /**
     * Возвращает объект SessionFactory, который используется для создания сессий Hibernate.
     * Если объект SessionFactory еще не создан, то создает его с помощью настроек, указанных в методе.
     * @return объект SessionFactory
     */
    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                logger.debug("SessionFactory opened");

                Configuration configuration = new Configuration();
                Properties settings = new Properties();
                settings.put(Environment.URL, "jdbc:mysql://localhost:3306/warehouses");
                settings.put(Environment.USER, "root");
                settings.put(Environment.PASS, "Umler1337228");
                settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");
                settings.put(Environment.SHOW_SQL, "false");
                settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
                settings.put(Environment.HBM2DDL_AUTO, "update");

                logger.debug("Settings put");
                configuration.setProperties(settings);
                configuration.addAnnotatedClass(User.class);
                configuration.addAnnotatedClass(Contract.class);
                configuration.addAnnotatedClass(Company.class);
                configuration.addAnnotatedClass(Room.class);
                configuration.addAnnotatedClass(Shelf.class);
                configuration.addAnnotatedClass(Product.class);

                logger.debug("Configuration added");

                logger.debug("ServiceRegistry");
                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();
                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }

    /**
     * Закрывает объект SessionFactory и освобождает все связанные с ним ресурсы.
     */
    public static void shutdown() {
        getSessionFactory().close();
    }
}
//package util;
//
//import org.hibernate.SessionFactory;
//import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
//import org.hibernate.cfg.Configuration;
//import org.hibernate.cfg.Environment;
//import org.hibernate.service.ServiceRegistry;
//
//import java.util.Properties;
//
///**
// * Code created by Andrius on 2020-09-26
// */
//public class HibernateUtil {
//
//    private static SessionFactory sessionFactory;
//
//    public static SessionFactory getSessionFactory() {
//        if (sessionFactory == null) {
//            try {
//                Configuration configuration = new Configuration();
//
//                Properties settings = new Properties();
//                settings.put(Environment.DRIVER, "org.h2.Driver");
//                settings.put(Environment.URL, "jdbc:h2:./src/main/resources/db/petclinic");
//                settings.put(Environment.USER, "sa");
//                settings.put(Environment.PASS, "");
//                settings.put(Environment.DIALECT, "org.hibernate.dialect.H2Dialect");
//                settings.put(Environment.SHOW_SQL, "true");
//                settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
//                settings.put(Environment.HBM2DDL_AUTO, "update");
//
//                configuration.setProperties(settings);
////                configuration.addAnnotatedClass(Veterinarian.class);
////                configuration.addAnnotatedClass(Pet.class);
////                configuration.addAnnotatedClass(Consult.class);
////                configuration.addAnnotatedClass(User.class);
//
//                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
//                        .applySettings(configuration.getProperties()).build();
//                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return sessionFactory;
//    }
//
//    public static void shutdown() {
////        getSessionFactory().close();
//    }
//}

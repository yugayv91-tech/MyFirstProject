package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            String dbUser = System.getenv("DB_USER");
            String dbPassword = System.getenv("DB_PASSWORD");

            if (dbUser == null || dbPassword == null) {
                throw new IllegalStateException(
                        "Не заданы переменные окружения DB_USER или DB_PASSWORD"
                );
            }

            Configuration cfg = new Configuration();

            cfg.setProperty(
                    "hibernate.connection.url",
                    "jdbc:mysql://localhost:3306/task_jdbc?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
            );

            cfg.setProperty("hibernate.connection.username", dbUser);
            cfg.setProperty("hibernate.connection.password", dbPassword);
            cfg.setProperty(
                    "hibernate.connection.driver_class",
                    "com.mysql.cj.jdbc.Driver"
            );

            cfg.setProperty(
                    "hibernate.dialect",
                    "org.hibernate.dialect.MySQL8Dialect"
            );

            cfg.setProperty("hibernate.show_sql", "true");
            cfg.setProperty("hibernate.hbm2ddl.auto", "none");

            cfg.addAnnotatedClass(User.class);

            return cfg.buildSessionFactory();

        } catch (Exception e) {
            throw new RuntimeException("Ошибка создания SessionFactory", e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
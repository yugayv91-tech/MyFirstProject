
package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class UserHibernateDaoImpl implements UserDao {

    public UserHibernateDaoImpl() {
    }

    // Создание таблицы пользователей
    @Override
    public void createUsersTable() {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.createNativeQuery(
                    "CREATE TABLE IF NOT EXISTS users (" +
                            "id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                            "name VARCHAR(50) NOT NULL, " +
                            "lastName VARCHAR(50) NOT NULL, " +
                            "age TINYINT NOT NULL)"
            ).executeUpdate();

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Ошибка создания таблицы users", e);
        }
    }

    // Удаление таблицы пользователей
    @Override
    public void dropUsersTable() {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.createNativeQuery("DROP TABLE IF EXISTS users")
                    .executeUpdate();

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Ошибка удаления таблицы users", e);
        }
    }

    // Добавление пользователя
    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            User user = new User(name, lastName, age);
            session.persist(user);

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Ошибка добавления пользователя", e);
        }
    }

    // Получение пользователя по ID
    @Override
    public User getUserById(long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.get(User.class, id);

        } catch (Exception e) {
            throw new RuntimeException("Ошибка получения пользователя по ID", e);
        }
    }

    // Получение всех пользователей
    @Override
    public List<User> getAllUsers() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {

            return session.createQuery("FROM User", User.class)
                    .getResultList();

        } catch (Exception e) {
            throw new RuntimeException("Ошибка получения пользователей", e);
        }
    }

    // Обновление пользователя
    @Override
    public void updateUser(User user) {
        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException(
                    "Пользователь и его ID не должны быть null"
            );
        }

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            User existingUser = session.get(User.class, user.getId());

            if (existingUser == null) {
                throw new IllegalArgumentException(
                        "Пользователь с ID " + user.getId() + " не найден"
                );
            }

            existingUser.setName(user.getName());
            existingUser.setLastName(user.getLastName());
            existingUser.setAge(user.getAge());

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Ошибка обновления пользователя", e);
        }
    }

    // Удаление пользователя по ID
    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            User user = session.get(User.class, id);

            if (user != null) {
                session.remove(user);
            }

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Ошибка удаления пользователя", e);
        }
    }

    // Очистка таблицы пользователей
    @Override
    public void cleanUsersTable() {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.createQuery("DELETE FROM User")
                    .executeUpdate();

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new RuntimeException("Ошибка очистки таблицы users", e);
        }
    }
}

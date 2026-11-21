
package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    public UserDaoJDBCImpl() {
    }

    // Создание таблицы пользователей
    @Override
    public void createUsersTable() {

        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(50), " +
                "lastName VARCHAR(50), " +
                "age TINYINT" +
                ")";

        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate(sql);

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка создания таблицы users", e);
        }
    }

    // Удаление таблицы пользователей
    @Override
    public void dropUsersTable() {

        String sql = "DROP TABLE IF EXISTS users";

        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate(sql);

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка удаления таблицы users", e);
        }
    }

    // Добавление пользователя
    @Override
    public void saveUser(String name, String lastName, byte age) {

        String sql = "INSERT INTO users (name, lastName, age) VALUES (?, ?, ?)";

        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка добавления пользователя", e);
        }
    }

    // Получение пользователя по ID
    @Override
    public User getUserById(long id) {

        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {
                    User user = new User(
                            resultSet.getString("name"),
                            resultSet.getString("lastName"),
                            resultSet.getByte("age")
                    );

                    user.setId(resultSet.getLong("id"));

                    return user;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка получения пользователя по ID", e);
        }

        return null;
    }

    // Обновление пользователя
    @Override
    public void updateUser(User user) {

        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException(
                    "Пользователь и его ID не должны быть null"
            );
        }

        String sql = "UPDATE users SET name = ?, lastName = ?, age = ? WHERE id = ?";

        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setByte(3, user.getAge());
            preparedStatement.setLong(4, user.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка обновления пользователя", e);
        }
    }

    // Удаление пользователя по ID
    @Override
    public void removeUserById(long id) {

        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, id);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка удаления пользователя", e);
        }
    }

    // Получение всех пользователей
    @Override
    public List<User> getAllUsers() {

        List<User> users = new ArrayList<>();

        String sql = "SELECT * FROM users";

        try (Connection connection = Util.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {

                User user = new User(
                        resultSet.getString("name"),
                        resultSet.getString("lastName"),
                        resultSet.getByte("age")
                );

                user.setId(resultSet.getLong("id"));

                users.add(user);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка получения пользователей", e);
        }

        return users;
    }

    // Очистка таблицы пользователей
    @Override
    public void cleanUsersTable() {

        String sql = "DELETE FROM users";

        try (Connection connection = Util.getConnection();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate(sql);

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка очистки таблицы users", e);
        }
    }
}

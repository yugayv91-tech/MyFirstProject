
package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;
import jm.task.core.jdbc.util.HibernateUtil;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        UserService userService = new UserServiceImpl();

        try {
            // Создаём таблицу
            userService.createUsersTable();

            // Очищаем таблицу перед тестированием
            userService.cleanUsersTable();

            // CREATE — добавляем четырёх пользователей
            userService.saveUser("Ivan", "Ivanov", (byte) 25);
            userService.saveUser("Petr", "Petrov", (byte) 30);
            userService.saveUser("Alex", "Sidorov", (byte) 35);
            userService.saveUser("Sergey", "Smirnov", (byte) 40);

            System.out.println("Пользователи добавлены.");

            // READ — получаем всех пользователей
            System.out.println("\nВсе пользователи:");

            List<User> users = userService.getAllUsers();

            for (User user : users) {
                System.out.println(user);
            }

            // READ — получаем пользователя по ID
            User user = userService.getUserById(users.get(0).getId());

            System.out.println("\nПользователь по ID:");
            System.out.println(user);

            // UPDATE — изменяем пользователя
            user.setName("Vladimir");
            user.setLastName("Ivanov");
            user.setAge((byte) 26);

            userService.updateUser(user);

            System.out.println("\nПосле обновления:");
            System.out.println(userService.getUserById(user.getId()));

            // DELETE — удаляем второго пользователя
            userService.removeUserById(users.get(1).getId());

            System.out.println("\nПосле удаления:");

            for (User remainingUser : userService.getAllUsers()) {
                System.out.println(remainingUser);
            }

            // Очищаем таблицу
            userService.cleanUsersTable();

            // Удаляем таблицу
            userService.dropUsersTable();

        } finally {
            // Закрываем SessionFactory
            HibernateUtil.getSessionFactory().close();
        }
    }
}

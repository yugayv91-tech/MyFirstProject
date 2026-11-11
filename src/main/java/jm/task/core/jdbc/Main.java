package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

public class Main {

    public static void main(String[] args) {

        UserService userService = new UserServiceImpl();

        // Создаём таблицу
        userService.createUsersTable();

        // Добавляем четырёх пользователей
        userService.saveUser("Ivan", "Ivanov", (byte) 25);
        System.out.println("Пользователь с именем - Ivan добавлен в базу данных");

        userService.saveUser("Petr", "Petrov", (byte) 30);
        System.out.println("Пользователь с именем - Petr добавлен в базу данных");

        userService.saveUser("Alex", "Sidorov", (byte) 35);
        System.out.println("Пользователь с именем - Alex добавлен в базу данных");

        userService.saveUser("Sergey", "Smirnov", (byte) 40);
        System.out.println("Пользователь с именем - Sergey добавлен в базу данных");

        // Получаем и выводим всех пользователей
        for (User user : userService.getAllUsers()) {
            System.out.println(user);
        }

        // Очищаем таблицу
        userService.cleanUsersTable();

        // Удаляем таблицу
        userService.dropUsersTable();
    }
}

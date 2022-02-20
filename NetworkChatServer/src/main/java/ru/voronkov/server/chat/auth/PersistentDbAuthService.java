package ru.voronkov.server.chat.auth;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class PersistentDbAuthService implements IAuthService {

    private static final Logger LOGGER = LogManager.getLogger(PersistentDbAuthService.class);

    private static final String DB_URL = "jdbc:sqlite:users.db";
    private Connection connection;
    private PreparedStatement getUsernameStatement;
    private PreparedStatement updateUsernameStatement;


    @Override
    public void start() {
        try {
            LOGGER.info("Подключение к базе данных пользователей...");
            connection = DriverManager.getConnection(DB_URL);
            LOGGER.info("Подключение к базе данных успешно.");
            getUsernameStatement = createGetUsernameStatement();
            updateUsernameStatement = createUpdateUsernameStatement();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            LOGGER.error("Ошиюка подключения к БД по ссылке: {}", DB_URL);
            throw new RuntimeException("Ошибка авторизации БД");
        }
    }

    @Override
    public String getUserNameByLoginAndPassword(String login, String password) {
        String username = null;
        try {
            getUsernameStatement.setString(1, login);
            getUsernameStatement.setString(2, password);
            ResultSet resultSet = getUsernameStatement.executeQuery();
            while (resultSet.next()) {
                username = resultSet.getString("user_name");
                break;
            }
            resultSet.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            LOGGER.error("Ошибка получения имени пользователя по паре : Login: {}; password: {}%n", login, password);
        }

        return username;
    }

    @Override
    public void updateUsername(String currentUsername, String newUsername) {
        try {
            updateUsernameStatement.setString(1, newUsername);
            updateUsernameStatement.setString(2, currentUsername);
            int result = updateUsernameStatement.executeUpdate();
            LOGGER.info("Обновление имени пользователя. Обновлено полей: {}", result);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            LOGGER.error("Ошибка обновления имени пользователя. искомое ИП: {}; новое ИП: {}%n",
                    currentUsername, newUsername);
        }
    }

    @Override
    public void stop() {
        if (connection != null) {
            try {
                LOGGER.info("Закрытие соединения с БД");
                connection.close();
                LOGGER.info("Соединение с БД успешно закрыто.");
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                LOGGER.error("Ошибка закрытия БД по URL: {}", DB_URL);
                throw new RuntimeException("Ошибка сервиса БД");
            }
        }
    }

    private PreparedStatement createGetUsernameStatement() throws SQLException {
        return connection.prepareStatement("SELECT user_name FROM users WHERE login = ? AND password = ? ");
    }


    private PreparedStatement createUpdateUsernameStatement() throws SQLException {
        return connection.prepareStatement("UPDATE users SET user_name = ? WHERE user_name = ? ");
    }
}
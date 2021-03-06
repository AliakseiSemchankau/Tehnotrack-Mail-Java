package ru.mail.track.message;

import ru.mail.track.data.DataService;

/**
 * Хранилище информации о пользователе
 */
public interface IUserStore {

    /**
     * Добавить пользователя в хранилище
     * Вернуть его же
     */
    User addUser(User user);

    /**
     *
     * Получить пользователя по логину/паролю
     */
    User getUser(String login, String pass);

    /**
     *
     * Получить пользователя по id, например запрос информации/профиля
     */
    User getUserById(Long id);

    /**
     *
     * It sets loader and loads a list of users
     */
    void initialize(DataService dService) throws Exception;

    boolean isUserExist(String login);

    void updateUserPass(User user);

    void close();

}
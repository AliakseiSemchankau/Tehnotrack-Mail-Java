package ru.mail.track;

import ru.mail.track.download.DownloadService;
import ru.mail.track.messageservice.Message;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by aliakseisemchankau on 29.9.15.
 */
public class UserStorage {

    private Map<String, List<Message>> commentHistory; // login -> list of comments
    private Map<String, Integer> commentCount; // login -> count of existing comments
    private Map<String, User> users;           // login -> corresponding User
    private DownloadService dService;          //downloading service for saving user information

    public UserStorage(final String userInfoDirectory, DownloadService dService) throws Exception {
        this.userInfoDirectory = userInfoDirectory;
        commentHistory = new HashMap<>();
        commentCount = new HashMap<>();

        this.dService = dService;
        dService.setUserInfoDirectory(userInfoDirectory);
    }

    public boolean isUserExist(String name) {
        return users.containsKey(name);
    }

    // Добавить пользователя в хранилище
    void addUser(User user) throws Exception {
        users.put(user.getName(), user);
        commentHistory.put(user.getName(), new LinkedList<>());
        commentCount.put(user.getName(), 0);

        dService.addUserName(user.getName());
        dService.addPassword(user.getHash());
    }

    // Получить пользователя по имени и паролю
    User getUser(String name, String pass) throws Exception {
        if (isUserExist(name) && AuthorizationService.isCorrect(users.get(name), pass)) {
            return users.get(name);
        }
        return null;
    }

    public List<Message> getUserCommentHistory(String userName) {

        return commentHistory.get(userName);

    }

    public void open() throws Exception {

        users = dService.downloadUsers();

        for (String userName : users.keySet()) {
            ArrayList<Message> userComments = dService.readCommentsHistoryUser(userName);
            commentHistory.put(userName, userComments);
            commentCount.put(userName, new Integer(userComments.size()));
        }

    }

    public void close() throws Exception {

        for (String userName : commentHistory.keySet()) {
            List<Message> comments = commentHistory.get(userName);

            if (comments.size() == commentCount.get(userName).intValue()) {  // if user didn't add any comment, we don't change anything
                continue;
            }

            List<Message> newComments = comments.subList(commentCount.get(userName).intValue(), comments.size()); //comments to update

            dService.appendCommentsForUser(newComments, userName);
        }

    }


}

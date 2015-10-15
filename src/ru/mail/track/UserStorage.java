package ru.mail.track;

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

    private String userInfoDirectory;

    private String fileLogins;
    private String filePasswords;

    private Map<String, List<String>> commentHistory; // login -> list of comments

    Map<String, User> users;

    public UserStorage(final String userInfoDirectory) throws Exception {
        this.userInfoDirectory = userInfoDirectory;
        this.fileLogins = userInfoDirectory + "/logins.txt";
        this.filePasswords = userInfoDirectory + "/passwords.txt";
        commentHistory = new TreeMap<String, List<String>>();

     }


    boolean isUserExist(String name) {
        return users.containsKey(name);
    }

    private void appendStringToFile(final String info, final String fileName) throws Exception {
        RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
        raf.skipBytes((int)raf.length());
        raf.writeBytes(info);
        raf.writeBytes("\n");
        raf.close();
    }

    private void appendPasswordToFile(final byte[] info, final String fileName) throws Exception {
        RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
        raf.skipBytes((int)raf.length());
        raf.write(info);
        raf.close();
    }

    // Добавить пользователя в хранилище
    void addUser(User user) throws Exception {
        users.put(user.getName(), user);
        appendStringToFile(user.getName(), fileLogins);
        appendPasswordToFile(user.getHash(), filePasswords);
        commentHistory.put(user.getName(), new LinkedList<>());
    }

    // Получить пользователя по имени и паролю
    User getUser(String name, String pass) throws Exception {
        if (isUserExist(name) && AuthorizationService.isCorrect(users.get(name), pass)) {
            return users.get(name);
        }
        return null;
    }

    public void open() throws Exception {

        BufferedReader br = new BufferedReader(new FileReader(fileLogins));
        FileInputStream fis = new FileInputStream(filePasswords);

        users = new HashMap<>();

        while (true) {
            String currentUserName = br.readLine();

            if (currentUserName != null) {
                byte[] currentHash = new byte[32];
                fis.read(currentHash);
                users.put(currentUserName, new User(currentUserName, currentHash));
            } else {
                break;
            }
        }

        br.close();
        fis.close();

        for(String userName : users.keySet()) {
            ArrayList<String> userComments = readCommentsHistoryFromFile(userName);
            commentHistory.put(userName, userComments);
        }

    }

    ArrayList<String> readCommentsHistoryFromFile(final String userName) {



        ArrayList<String> comments = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(userInfoDirectory + "/" + userName + ".txt"));
            String currentComment;
            while(true) {
                currentComment = br.readLine();
                if (currentComment == null) {
                    break;
                }
               comments.add(currentComment);
            }
        } catch (Exception exc) {
            return comments;
        }
        return comments;
    }

    public List<String> getUserCommentHistory(String userName) {

        return commentHistory.get(userName);

    }

    public void close() throws Exception {

        for(String userName : commentHistory.keySet()) {
            List<String> comments = commentHistory.get(userName);
            BufferedWriter bw = new BufferedWriter(new FileWriter(userInfoDirectory + "/" + userName + ".txt"));
            for(String comment : comments) {
                bw.write(comment + "\n");
            }
            bw.close();
        }

    }

}

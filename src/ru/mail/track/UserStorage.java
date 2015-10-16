package ru.mail.track;

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

    private String userInfoDirectory;

    private String fileLogins;
    private String filePasswords;

    private Map<String, List<Message>> commentHistory; // login -> list of comments
    private Map<String, Integer> commentCount; // login -> count of existing comments

    Map<String, User> users;

    public UserStorage(final String userInfoDirectory) throws Exception {
        this.userInfoDirectory = userInfoDirectory;
        this.fileLogins = userInfoDirectory + "/logins.txt";
        this.filePasswords = userInfoDirectory + "/passwords.txt";
        commentHistory = new TreeMap<String, List<Message>>();
        commentCount = new TreeMap<>();
    }

    boolean isUserExist(String name) {
        return users.containsKey(name);
    }

    private void appendStringToFile(final String info, final String fileName) throws Exception {
        RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
        raf.skipBytes((int) raf.length());
        raf.writeBytes(info);
        raf.writeBytes("\n");
        raf.close();
    }

    private void appendPasswordToFile(final byte[] info, final String fileName) throws Exception {
        RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
        raf.skipBytes((int) raf.length());
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

        for (String userName : users.keySet()) {
            ArrayList<Message> userComments = readCommentsHistoryFromFile(userName);
            commentHistory.put(userName, userComments);
            commentCount.put(userName, new Integer(userComments.size()));
        }

    }

    ArrayList<Message> readCommentsHistoryFromFile(final String userName) {

        ArrayList<Message> comments = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(userInfoDirectory + "/" + userName + ".txt"));
            String currentTime;
            String currentComment;
            while (true) {
                currentTime = br.readLine();
                currentComment = br.readLine();
                if (currentTime == null || currentComment == null) {
                    break;
                }
                comments.add(new Message(currentComment, currentTime));
            }
        } catch (Exception exc) {
            return comments;
        }
        return comments;
    }

    public List<Message> getUserCommentHistory(String userName) {

        return commentHistory.get(userName);

    }

    public void close() throws Exception {

        for (String userName : commentHistory.keySet()) {
            List<Message> comments = commentHistory.get(userName);
            //System.out.println(commentCount.get(userName).intValue() + " = count for " + userNameq);
            if (comments.size() == commentCount.get(userName).intValue()) {
                continue;
            }
            System.out.println(commentCount.get(userName).intValue() + " " + (comments.size() - 1) + " " + userName);
            List<Message> newComments = comments.subList(commentCount.get(userName).intValue(), comments.size());
            appendCommentsToFile(newComments, userInfoDirectory + "/" + userName + ".txt");
        }

    }

    private void appendCommentsToFile(List<Message> comments, final String fileName) {


        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)))) {
            for (Message msg : comments) {
                out.write(msg.getTimeStamp() + "\n" + msg.getMessage() + "\n");
            }

        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }

    }

}

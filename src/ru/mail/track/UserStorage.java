package ru.mail.track;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by aliakseisemchankau on 29.9.15.
 */
public class UserStorage {

    private String fileLogins;
    private String filePasswords;

    Map<String, User> users;

    public UserStorage(final String fileLogins, final String filePasswords) throws Exception {
        this.fileLogins = fileLogins;
        this.filePasswords = filePasswords;
        users = new HashMap<>();
    }


    boolean isUserExist(String name) {
        return users.containsKey(name);
    }

    private void appendStringToFile(final String info, final String fileName) throws Exception {
        FileWriter fw = new FileWriter(fileName);
        for (char c : info.toCharArray()) {
            fw.append(c);
        }
        fw.close();
    }

    // Добавить пользователя в хранилище
    void addUser(User user) throws Exception {
        users.put(user.getName(), user);

        appendStringToFile(user.getName(), fileLogins);

        String bytes = new String(user.getHash());
        appendStringToFile(bytes, filePasswords);
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
    }

    /*
    public void close() throws Exception {

        BufferedWriter bw = new BufferedWriter(new FileWriter(fileLogins));
        FileOutputStream fos = new FileOutputStream(filePasswords);
        for (Map.Entry<String, User> entry : users.entrySet()) {
            String currentUserName = entry.getKey();
            byte[] currentHash = entry.getValue().getHash();
            bw.write(currentUserName + "\n");
            fos.write(currentHash);
        }

        bw.close();
        fos.close();
    }
    */

}

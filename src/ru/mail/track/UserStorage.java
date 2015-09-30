package ru.mail.track;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by aliakseisemchankau on 29.9.15.
 */
public class UserStorage {

    Map<String, User> users;

    public UserStorage(final String fileLogins, final String filePasswords) throws Exception {
        users = new HashMap<>();

        BufferedReader br = new BufferedReader(new FileReader(fileLogins));
        FileInputStream fis = new FileInputStream(filePasswords);

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
    }


    boolean isUserExist(String name) {
        return users.containsKey(name);
    }

    // Добавить пользователя в хранилище
    void addUser(User user) {
        users.put(user.getName(), user);
    }

    // Получить пользователя по имени и паролю
    User getUser(String name, String pass) throws Exception {
        if (isUserExist(name) && users.get(name).isCorrect(pass)) {
            return users.get(name);
    } else {
            return null;
        }
}

    public void close(final String fileLogins, final String filePasswords) throws IOException{

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

}

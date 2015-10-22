package ru.mail.track.download;

import ru.mail.track.User;
import ru.mail.track.messageservice.Message;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by aliakseisemchankau on 22.10.15.
 */
public class DownloadServiceFileImpl implements DownloadService{

    private String userInfoDirectory;
    private String fileLogins;
    private String filePasswords;

    @Override
    public void setUserInfoDirectory(String userInfoDirectory) {
        this.userInfoDirectory = userInfoDirectory;
        fileLogins = userInfoDirectory + "/logins.txt";
        filePasswords = userInfoDirectory + "/passwords.txt";
    }

    @Override
    public HashMap<String, User> downloadUsers() throws Exception{

        HashMap<String, User> users = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileLogins));
             FileInputStream fis = new FileInputStream(filePasswords)) {

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

        } catch (Exception exc) {
            throw new Exception("can't download " + fileLogins + " or " + filePasswords);
        }

        return users;

    }

    @Override
    public ArrayList<Message> readCommentsHistoryUser(String userName) {

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

    @Override
    public void addUserName(String userName) throws Exception{
        appendStringToFile(userName, fileLogins);

    }

    @Override
    public void addPassword(byte[] password) throws Exception{
        appendPasswordToFile(password, filePasswords);
    }

    @Override
    public void appendCommentsForUser(List<Message> comments, final String userName) {

        String fileName = userInfoDirectory + "/" + userName + ".txt";

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(fileName, true)))) {
            for (Message msg : comments) {
                out.write(msg.getTimeStamp() + "\n" + msg.getMessage() + "\n");
            }

        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }

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

}

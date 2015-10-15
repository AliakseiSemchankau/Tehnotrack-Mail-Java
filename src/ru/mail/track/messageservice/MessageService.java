package ru.mail.track.messageservice;

import ru.mail.track.UserStorage;
import ru.mail.track.messageservice.perform.*;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;



/**
 * Created by aliakseisemchankau on 13.10.15.
 */
public class MessageService {

    List<String> commentsHistory;
    String nickName;
    boolean exit = false;
    Map<String, MessageServiceActionPerformer> commandPerformer = new TreeMap<>();

    public MessageService(String userName, UserStorage store) {
        commentsHistory = store.getUserCommentHistory(userName);
        nickName = userName;
        commandPerformer.put("\\help", new MessageServiceActionPerformerHelp());
        commandPerformer.put("\\user", new MessageServiceActionPerformerUser());
        commandPerformer.put("\\history", new MessageServiceActionPerformerHistory());
        commandPerformer.put("\\find", new MessageServiceActionPerformerFind());
        commandPerformer.put("\\exit", new MessageServiceActionPerformerExit());
    }


    public void start() {

        System.out.println("hello to your work note!");

        Scanner scan = new Scanner(System.in);

        while (!exit) {
            String curMessage = scan.nextLine();
            curMessage = curMessage.trim();
            if (isCommand(curMessage)) {
                perform(curMessage);
            } else {
                commentsHistory.add(curMessage);
            }
        }

    }

    private boolean isCommand(String message) {
        if (message == null || message.length() == 0) {
            return false;
        }
        return (message.charAt(0) == '\\');
    }

    private void perform(String message) {
        String[] words = message.split(" ");
        if (commandPerformer.containsKey(words[0])) {
            commandPerformer.get(words[0]).perform(words, this);
        } else {
            System.out.println("there is no such command as " + words[0]);
        }
    }

    public String getNickName(){
        return nickName;
    }

    public void setNickName(final String newNickName) {
        this.nickName = newNickName;
    }

    public void mustExit() {
        exit = true;
    }

    public List<String> getCommentsHistory() {
        return commentsHistory;
    }

}

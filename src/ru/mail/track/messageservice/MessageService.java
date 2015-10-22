package ru.mail.track.messageservice;

import ru.mail.track.User;
import ru.mail.track.UserStorage;
import ru.mail.track.messageservice.perform.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


/**
 * Created by aliakseisemchankau on 13.10.15.
 */
public class MessageService {

    private List<Message> commentsHistory;
    private boolean exit = false;
    private Map<String, MessageServiceCommand> commandPerformer = new HashMap<>();
    private User user;

    public MessageService(User user, UserStorage store) {
        this.user = user;
        commentsHistory = store.getUserCommentHistory(user.getName());
        commandPerformer.put("\\help", new MessageServiceCommandHelp());
        commandPerformer.put("\\user", new MessageServiceCommandUser());
        commandPerformer.put("\\history", new MessageServiceCommandHistory());
        commandPerformer.put("\\find", new MessageServiceCommandFind());
        commandPerformer.put("\\exit", new MessageServiceCommandExit());
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
                commentsHistory.add(new Message(curMessage));
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
        return user.getNickName();
    }

    public void setNickName(final String newNickName) {
        user.setNickName(newNickName);
    }

    public void mustExit() {
        exit = true;
    }

    public List<Message> getCommentsHistory() {
        return commentsHistory;
    }

}

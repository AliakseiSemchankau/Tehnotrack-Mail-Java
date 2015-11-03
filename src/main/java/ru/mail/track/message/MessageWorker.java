package ru.mail.track.message;

import ru.mail.track.perform.*;
import ru.mail.track.session.Session;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


/**
 * Created by aliakseisemchankau on 13.10.15.
 */


//TODO: rename to CommandHandler
public class MessageWorker {

    private Map<String, CommandHandler> commandPerformer = new HashMap<>();
    private UserStorage userStore;
    private MessageStore messageStore;

    public MessageWorker(User user, UserStorage store, MessageStore messageStore) {
        this.userStore = store;
        this.messageStore = messageStore;

        //TODO add command LOGIN <user> <pass>
        commandPerformer.put("\\login", new CommandHandlerLogin());
        commandPerformer.put("\\help", new CommandHandlerHelp());
        commandPerformer.put("\\user", new CommandHandlerUser());
        commandPerformer.put("\\history", new CommandHandlerHistory());
        commandPerformer.put("\\find", new CommandHandlerFind());
    }

/*
    public void start() throws Exception{

        System.out.println("hello to your work note!");

        Scanner scan = new Scanner(System.in);

        try {
            while (!exit) {
                String curMessage = scan.nextLine();
                curMessage = curMessage.trim();
                if (isCommand(curMessage)) {
                    perform(new Message(curMessage));
                } else {
                    commentsHistory.add(new Message(curMessage));
                }
            }
        } finally {
            store.saveUserChanges(user.getName());
        }

    }
    */

    private boolean isCommand(String message) {
        if (message == null || message.length() == 0) {
            return false;
        }
        return (message.charAt(0) == '\\');
    }

    public Result work(Message msg, Session session) {
        String[] words = msg.getMessage().split(" ");
        if (commandPerformer.containsKey(words[0])) {
            return commandPerformer.get(words[0]).perform(words, session, userStore, messageStore);
        } else {
            System.out.println("there is no such command as " + words[0]);
        }
    }

}

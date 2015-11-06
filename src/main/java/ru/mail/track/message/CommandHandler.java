package ru.mail.track.message;

import ru.mail.track.message.messagetypes.Message;
import ru.mail.track.perform.*;
import ru.mail.track.session.Session;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by aliakseisemchankau on 13.10.15.
 */


//TODO: rename to Command
public class CommandHandler {

    private Map<CommandType, Command> commandPerformer = new HashMap<>();
    private UserStorage userStore;
    private MessageStore messageStore;

    public CommandHandler(UserStorage store, MessageStore messageStore) {
        this.userStore = store;
        this.messageStore = messageStore;

        //TODO add command LOGIN <user> <pass>
        commandPerformer.put(CommandType.USER_LOGIN, new CommandLogin());
        commandPerformer.put(CommandType.USER_HELP, new CommandHelp());
        commandPerformer.put(CommandType.CHAT_HISTORY, new CommandChatHistory());
        commandPerformer.put(CommandType.CHAT_FIND, new CommandChatFind());
        commandPerformer.put(CommandType.USER_REGISTER, new CommandRegister());
        commandPerformer.put(CommandType.USER_INFO, new CommandInfo());
        commandPerformer.put(CommandType.USER_PASS, new CommandPass());
        commandPerformer.put(CommandType.CHAT_LIST, new CommandChatList());
        commandPerformer.put(CommandType.CHAT_CREATE, new CommandChatCreate());
        commandPerformer.put(CommandType.CHAT_FIND, new CommandChatFind());
        commandPerformer.put(CommandType.CHAT_SEND, new CommandChatSend());
        commandPerformer.put(CommandType.CHAT_HISTORY, new CommandChatHistory());
    }


    public Result work(Message msg, Session session) {

        if (commandPerformer.containsKey(msg.getType())) {
            return commandPerformer.get(msg.getType()).perform(msg, session, userStore, messageStore);
        }
        return new Result(false, "such command as " + msg.getType() + " does not exist");
    }

}

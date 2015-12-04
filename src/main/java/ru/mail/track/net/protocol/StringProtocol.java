package ru.mail.track.net.protocol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.mail.track.message.messagetypes.ChatCreateMessage;
import ru.mail.track.message.messagetypes.ChatFindMessage;
import ru.mail.track.message.messagetypes.ChatHistoryMessage;
import ru.mail.track.message.messagetypes.ChatListMessage;
import ru.mail.track.message.messagetypes.HelpMessage;
import ru.mail.track.message.messagetypes.InfoMessage;
import ru.mail.track.message.messagetypes.PassMessage;
import ru.mail.track.message.messagetypes.RegisterMessage;
import ru.mail.track.message.messagetypes.SimpleMessage;
import ru.mail.track.perform.CommandType;
import ru.mail.track.message.messagetypes.LoginMessage;
import ru.mail.track.message.messagetypes.Message;
import ru.mail.track.message.messagetypes.ChatSendMessage;

import java.util.List;

/**
 *
 */
public class StringProtocol implements Protocol {

    static Logger log = LoggerFactory.getLogger(StringProtocol.class);

    public static final String DELIMITER = ";";

    @Override
    public Message decode(byte[] bytes) {
        String str = new String(bytes);
        log.info("STRING PROTOCOL:decoded:[\n {} \n]", str);
        String[] tokens = str.split(DELIMITER);
        CommandType type = CommandType.valueOf(tokens[0]);

        switch (type) {
            case USER_LOGIN:
                LoginMessage loginMessage = new LoginMessage();
                loginMessage.setLogin(tokens[1]);
                loginMessage.setPassword(tokens[2]);
                return loginMessage;
            case CHAT_SEND:
                ChatSendMessage chatSendMessage = new ChatSendMessage();
                chatSendMessage.setChatId(Long.valueOf(tokens[1]));
                StringBuilder textMsg = new StringBuilder();
                for(int i = 2; i < tokens.length; ++i) {
                    textMsg.append(tokens[i] + " ");
                }
                chatSendMessage.setMessage(textMsg.toString());
                return chatSendMessage;
            case SIMPLE_MSG:
                SimpleMessage simpleMessage = new SimpleMessage();
                simpleMessage.setMessage(tokens[1]);
                return simpleMessage;
            case USER_REGISTER:
                RegisterMessage registerMessage = new RegisterMessage();
                registerMessage.setLogin(tokens[1]);
                registerMessage.setPassword(tokens[2]);
                return registerMessage;
            case USER_HELP:
                HelpMessage helpMessage = new HelpMessage();
                return helpMessage;
            case USER_INFO:
                InfoMessage infoMessage = new InfoMessage();
                infoMessage.setHasArg(Boolean.valueOf(tokens[1]));
                if (infoMessage.isHasArg()) {
                    infoMessage.setUserId(Long.valueOf(tokens[2]));
                }
                return infoMessage;
            case USER_PASS:
                PassMessage passMessage = new PassMessage();
                passMessage.setOldPass(tokens[1]);
                passMessage.setNewPass(tokens[2]);
                return passMessage;
            case CHAT_LIST:
                ChatListMessage chatListMessage = new ChatListMessage();
                return chatListMessage;
            case CHAT_CREATE:
                ChatCreateMessage chatCreateMessage = new ChatCreateMessage();
                for (int i = 1; i < tokens.length; ++i) {
                    chatCreateMessage.addId(Long.valueOf(tokens[i]));
                }
                return chatCreateMessage;
            case CHAT_HISTORY:
                ChatHistoryMessage chatHistoryMessage = new ChatHistoryMessage();
                chatHistoryMessage.setChatId(Long.valueOf(tokens[1]));
                chatHistoryMessage.setHasArg(Boolean.valueOf(tokens[2]));
                if (chatHistoryMessage.isHasArg()) {
                    chatHistoryMessage.setCountOfMessages(Long.valueOf(tokens[3]));
                }
                return chatHistoryMessage;
            case CHAT_FIND:
                ChatFindMessage chatFindMessage = new ChatFindMessage();
                chatFindMessage.setChatId(Long.valueOf(tokens[1]));
                chatFindMessage.setPattern(tokens[2]);
               return chatFindMessage;
            default:
                throw new RuntimeException("Invalid type: " + type);
        }
    }

    @Override
    public byte[] encode(Message msg) {
        StringBuilder builder = new StringBuilder();
        CommandType type = msg.getType();
        builder.append(type).append(DELIMITER);
        switch (type) {
            case USER_LOGIN:
                LoginMessage loginMessage = (LoginMessage) msg;
                builder.append(loginMessage.getLogin()).append(DELIMITER);
                builder.append(loginMessage.getPassword()).append(DELIMITER);
                break;
            case CHAT_SEND:
                ChatSendMessage chatSendMessage = (ChatSendMessage) msg;
                builder.append(chatSendMessage.getChatId()).append(DELIMITER);
                builder.append(chatSendMessage.getMessage()).append(DELIMITER);
                break;
            case SIMPLE_MSG:
                SimpleMessage simpleMessage = (SimpleMessage) msg;
                builder.append(simpleMessage.getMessage()).append(DELIMITER);
                break;
            case USER_REGISTER:
                RegisterMessage registerMessage = (RegisterMessage) msg;
                builder.append(registerMessage.getLogin()).append(DELIMITER);
                builder.append(registerMessage.getPassword()).append(DELIMITER);
                break;
            case USER_HELP:
                HelpMessage helpMessage = (HelpMessage) msg;
                break;
            case USER_INFO:
                InfoMessage infoMessage = (InfoMessage) msg;
                builder.append(infoMessage.isHasArg()).append(DELIMITER);
                if (infoMessage.isHasArg()) {
                    builder.append(infoMessage.getUserId()).append(DELIMITER);
                }
                break;
            case USER_PASS:
                PassMessage passMessage = (PassMessage) msg;
                builder.append(passMessage.getOldPass()).append(DELIMITER);
                builder.append(passMessage.getNewPass()).append(DELIMITER);
                break;
            case CHAT_LIST:
                ChatListMessage chatListMessage = (ChatListMessage) msg;
                break;
            case CHAT_CREATE:
                ChatCreateMessage chatCreateMessage = (ChatCreateMessage) msg;
                List<Long> ids = chatCreateMessage.getChatUserIds();
                for (Long id : ids) {
                    builder.append(id.toString()).append(DELIMITER);
                }
                break;
            case CHAT_FIND:
                ChatFindMessage chatFindMessage = (ChatFindMessage) msg;
                builder.append(chatFindMessage.getChatId().toString()).append(DELIMITER);
                builder.append(chatFindMessage.getPattern()).append(DELIMITER);
                break;
            case CHAT_HISTORY:
                ChatHistoryMessage chatHistoryMessage = (ChatHistoryMessage) msg;
                builder.append(chatHistoryMessage.getChatId().toString()).append(DELIMITER);
                builder.append(chatHistoryMessage.isHasArg()).append(DELIMITER);
                if (chatHistoryMessage.isHasArg()) {
                    builder.append(chatHistoryMessage.getCountOfMessages()).append(DELIMITER);
                }
                break;

            default:
                throw new RuntimeException("Invalid type: " + type);

        }
        log.info("STRING PROTOCOL:encoded:{}", builder.toString());
        return builder.toString().getBytes();
    }


}

package ru.mail.track.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.mail.track.message.messagetypes.HelpMessage;
import ru.mail.track.message.messagetypes.InfoMessage;
import ru.mail.track.message.messagetypes.RegisterMessage;
import ru.mail.track.message.messagetypes.SimpleMessage;
import ru.mail.track.perform.CommandType;
import ru.mail.track.message.messagetypes.LoginMessage;
import ru.mail.track.message.messagetypes.Message;
import ru.mail.track.message.messagetypes.SendMessage;

import javax.sound.midi.MidiDevice;

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
            case MSG_SEND:
                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(Long.valueOf(tokens[1]));
                sendMessage.setMessage(tokens[2]);
                return sendMessage;
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
                HelpMessage helpMessage =  new HelpMessage();
                return helpMessage;
            case USER_INFO:
                InfoMessage infoMessage = new InfoMessage();
                infoMessage.setHasArg(Boolean.valueOf(tokens[1]));
                if (infoMessage.isHasArg()) {
                    infoMessage.setUserId(Long.valueOf(tokens[2]));
                }
                return infoMessage;
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
            case MSG_SEND:
                SendMessage sendMessage = (SendMessage) msg;
                builder.append(sendMessage.getChatId()).append(DELIMITER);
                builder.append(sendMessage.getMessage()).append(DELIMITER);
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
            default:
                throw new RuntimeException("Invalid type: " + type);

        }
        log.info("STRING PROTOCOL:encoded:{}", builder.toString());
        return builder.toString().getBytes();
    }


}

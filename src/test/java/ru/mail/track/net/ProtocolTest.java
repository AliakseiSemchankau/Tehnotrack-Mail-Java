package ru.mail.track.net;

import org.junit.Before;
import org.junit.Test;
import ru.mail.track.message.messagetypes.ChatSendMessage;
import ru.mail.track.message.messagetypes.LoginMessage;
import ru.mail.track.message.messagetypes.Message;
import ru.mail.track.perform.CommandType;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by aliakseisemchankau on 10.11.15.
 */
public class ProtocolTest {

    private final Map<CommandType, Message> messages = new HashMap<>();
    private Protocol serProtocol;
    private Protocol refProtocol;

    @Before
    public void setup() {

        serProtocol = new SerializableProtocol();
        refProtocol = new ReflectionProtocol();

        LoginMessage login = new LoginMessage();
        login.setSender(123L);
        login.setLogin("Jack");
        login.setPassword("qwerty");
        messages.put(CommandType.USER_LOGIN, login);

        ChatSendMessage send = new ChatSendMessage();
        send.setChatId(1L);
        send.setMessage("Hello world!");
        messages.put(CommandType.CHAT_SEND, send);

    }

    @Test
    public void testSerLogin() throws Exception {

        Message origin = messages.get(CommandType.USER_LOGIN);
        byte[] data = serProtocol.encode(origin);
        Message copy = serProtocol.decode(data);
        assertEquals(origin, copy);
    }

    @Test
    public void testSerSend() throws Exception {
        Message origin = messages.get(CommandType.CHAT_SEND);
        byte[] data = serProtocol.encode(origin);
        Message copy = serProtocol.decode(data);
        assertEquals(origin, copy);
    }

    @Test
    public void testRefLogin() throws Exception {

        Message origin = messages.get(CommandType.USER_LOGIN);
        byte[] data = refProtocol.encode(origin);
        Message copy = refProtocol.decode(data);
        assertEquals(origin, copy);
    }

    @Test
    public void testRefSend() throws Exception {
        Message origin = messages.get(CommandType.CHAT_SEND);
        byte[] data = refProtocol.encode(origin);
        Message copy = refProtocol.decode(data);
        assertEquals(origin, copy);
    }

}
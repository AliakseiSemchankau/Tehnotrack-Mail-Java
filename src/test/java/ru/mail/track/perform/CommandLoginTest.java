package ru.mail.track.perform;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ru.mail.track.data.DataServiceDBImpl;
import ru.mail.track.message.MessageStore;
import ru.mail.track.message.MessageStoreStub;
import ru.mail.track.message.User;
import ru.mail.track.message.UserStorage;
import ru.mail.track.message.messagetypes.LoginMessage;
import ru.mail.track.net.SessionManager;
import ru.mail.track.session.Session;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import static org.mockito.Mockito.*;

/**
 * Created by aliakseisemchankau on 10.11.15.
 */
public class CommandLoginTest {

    UserStorage userStore;
    SessionManager sessionManager;
    User defaultUser = new User("Jack", "qwerty");
    MessageStore messageStore = new MessageStoreStub(new DataServiceDBImpl());

    @Before
    public void setup() {
        sessionManager = new SessionManager();
        userStore =  Mockito.mock(UserStorage.class);
        when(userStore.getUser("Jack", "qwerty")).thenReturn(defaultUser);

    }

    @Test
    public void successLogin() throws Exception {

        CommandLogin loginCommand = new CommandLogin();
        LoginMessage login = new LoginMessage();
        login.setSender(123L);
        login.setLogin("Jack");
        login.setPassword("qwerty");

        Session session = new Session();
        session.setSessionManager(sessionManager);
        session.setSessionUser(defaultUser);

        loginCommand.perform(login, session, userStore, messageStore);
        assertEquals(session.getSessionUser(), defaultUser);
    }

    @Test
    public void badLogin() throws Exception {

        CommandLogin loginCommand = new CommandLogin();
        LoginMessage login = new LoginMessage();
        login.setSender(123L);
        login.setLogin("Jack");
        login.setPassword("*****");

        Session session = new Session();
        loginCommand.perform(login, session, userStore, messageStore);
        assertNull(session.getSessionUser());
    }


}

package ru.mail.track.perform;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import ru.mail.track.data.DataService;
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
 * Created by aliakseisemchankau on 12.11.15.
 */
public class DataServiceDBImplTest {

    DataService dataService;

    @Before
    public void setup() throws Exception {
        dataService = new DataServiceDBImpl();
        dataService.init();
    }

    @Test
    public void testGetUserById() throws Exception {

        User userTest1 = new User("aq");
        userTest1.setHash("-15565897578092285804301746388437541736718987028345894595833970893285724925004");
        userTest1.setUserID(Long.valueOf(1));

        User user1 = dataService.getUserById(Long.valueOf(1));

        assertEquals(userTest1, user1);

        User userTest3 = new User("new");
        userTest3.setHash("7831508586579878274124407691678839497946039999552402781250656632284863644727");
        userTest3.setUserID(Long.valueOf(3));

        User user3 = dataService.getUserById(Long.valueOf(3));

        assertEquals(userTest3, user3);

        User user0 = dataService.getUserById(Long.valueOf(0));
        assertNull(user0);

        User user1000 = dataService.getUserById(Long.valueOf(1000));
        assertNull(user1000);

    }


}
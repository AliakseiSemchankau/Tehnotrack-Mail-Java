package ru.mail.track.data;

import ru.mail.track.message.User;
import ru.mail.track.message.messagetypes.Message;

import java.util.List;
import java.util.Map;

/**
 * Created by aliakseisemchankau on 6.11.15.
 */
public class DataServiceDBImpl implements DataService{
    @Override
    public void init() {



    }

    @Override
    public Map<Long, User> downloadUsers() throws Exception {
        return null;
    }

    @Override
    public List<Message> readCommentsHistoryUser(String userName) {
        return null;
    }

    @Override
    public void addUser(String userName, byte[] password, Long userId) throws Exception {

    }

    @Override
    public void addUserName(String userName) throws Exception {

    }

    @Override
    public void addPassword(byte[] password) throws Exception {

    }

    @Override
    public void appendCommentsForUser(List<Message> comments, String userName) {

    }
}

package ru.mail.track.download;

import ru.mail.track.User;
import ru.mail.track.messageservice.Message;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by aliakseisemchankau on 22.10.15.
 */
public interface DownloadService {

    public void init();

    public Map<String, User> downloadUsers() throws Exception;

    public List<Message> readCommentsHistoryUser(String userName);

    public void addUserName(String userName) throws Exception;

    public void addPassword(byte[] password) throws Exception;

    public void appendCommentsForUser(List<Message> comments, final String userName);

}

package ru.mail.track.data;

import org.postgresql.ds.PGPoolingDataSource;
import ru.mail.track.jdbc.QueryExecutor;
import ru.mail.track.jdbc.ResultHandler;
import ru.mail.track.message.Chat;
import ru.mail.track.message.User;
import ru.mail.track.message.messagetypes.Message;
import ru.mail.track.perform.CommandType;

import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by aliakseisemchankau on 6.11.15.
 */
public class DataServiceDBImpl implements DataService {

    QueryExecutor queryExecutor;

    @Override
    public void init() throws Exception {

        Class.forName("org.postgresql.Driver");

        PGPoolingDataSource source = new PGPoolingDataSource();
        source.setDataSourceName("My DB");
        source.setServerName("178.62.140.149");
        source.setDatabaseName("AliakseiSemchankau");
        source.setUser("senthil");
        source.setPassword("ubuntu");
        source.setMaxConnections(10);

        Connection c = source.getConnection();

        queryExecutor = new QueryExecutor();
        queryExecutor.setConnection(c);


    }

    @Override
    public User getUser(String userName) throws Exception {

        String sql = "" +
                "SELECT id, login, password " +
                "FROM users " +
                "WHERE login = ?";

        Map<Integer, Object> prepared = new HashMap<>();
        prepared.put(1, userName);

        User user = queryExecutor.execQuery(sql, prepared, new ResultHandler<User>() {
            @Override
            public User handle(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    String login = resultSet.getString("login");
                    String hash = resultSet.getString("password");
                    Long id = resultSet.getLong("id");
                    User user = new User(login);
                    user.setHash(hash);
                    user.setUserID(id);
                    return user;
                }
                return null;
            }
        });

        return user;
    }

    @Override
    public User getUserById(Long id) throws Exception {
        String sql = "" +
                "SELECT login, password " +
                "FROM users " +
                "WHERE id = ? ";

        Map<Integer, Object> prepared = new HashMap<>();
        prepared.put(1, id);

        User user = queryExecutor.execQuery(sql, prepared, new ResultHandler<User>() {
            @Override
            public User handle(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    String login = resultSet.getString("login");
                    String hash = resultSet.getString("password");
                    User user = new User(login);
                    user.setHash(hash);
                    return user;
                }
                return null;
            }
        });
        if (user != null) {
            user.setUserID(id);
        }
        return user;
    }

    @Override
    public void setNewPass(String login, String password) throws Exception {

        String sqlUpdate = ""
                + "UPDATE users "
                + "SET password = ? "
                + "WHERE login = ? ;";

        Map<Integer, Object> prepared = new HashMap<>();
        prepared.put(1, login);
        prepared.put(2, password);

        queryExecutor.updateQuery(sqlUpdate, prepared);

    }

    @Override
    public void addChat(Chat chat) {

        String sqlInsert = "INSERT INTO chats (user_id) VALUES " +
                "(?) ;";

        Map<Integer, Object> prepared = new HashMap<>();
        prepared.put(1, chat.getCreatorId());

        Long key = null;
        try {
            key = queryExecutor.updateQueryWithGeneratedKey(sqlInsert, prepared, "chat_id");
            System.out.println("key = " + key);
        } catch (SQLException sqlExc) {
            System.err.println("troubles with sql query=" + sqlInsert + ", where ?=" + chat.getCreatorId());
            sqlExc.printStackTrace();
        }

        chat.setId(key);

    }

    @Override
    public void addUserToChat(Long userId, Long chatId) {

        String sqlInsert = "INSERT INTO userchats (\"chat_id\", \"user_id\") " +
                "VALUES " +
                "(?, ?); ";

        Map<Integer, Object> prepared = new HashMap<>();
        prepared.put(1, chatId);
        prepared.put(2, userId);

        try {
            queryExecutor.updateQuery(sqlInsert, prepared);
        } catch (SQLException sqlExc) {
            System.err.println("some troubles occured while parsing this:");
            System.err.println(sqlInsert);
            System.err.println("where ?=" + chatId + ", ?=" + userId);
            sqlExc.printStackTrace();
        }
    }

    @Override
    public List<Long> getChatsByUserId(Long userId) {

        String sql = "SELECT chat_id " +
                "FROM userchats " +
                "WHERE user_id = ?";

        Map<Integer, Object> prepared = new HashMap<>();
        prepared.put(1, userId);

        List<Long> chatList = null;
        try {
            chatList = queryExecutor.execQuery(sql, prepared, new ResultHandler<List<Long>>() {
                @Override
                public List<Long> handle(ResultSet resultSet) throws SQLException {
                    List<Long> chatList = new ArrayList<Long>();
                    while (resultSet.next()) {
                        chatList.add(resultSet.getLong("chat_id"));
                    }
                    return chatList;
                }
            });
        } catch (SQLException sqlExc) {
            System.err.println("some troubles with sql=\n" + sql);
            System.err.println("?=" + userId);
            sqlExc.printStackTrace();
        }
        return chatList;
    }

    @Override
    public List<Long> getUsersByChatId(Long chatId) {
        String sql = "SELECT user_id " +
                "FROM userchats " +
                "WHERE chat_id = ?";

        Map<Integer, Object> prepared = new HashMap<>();
        prepared.put(1, chatId);

        List<Long> userList = null;
        try {
            userList = queryExecutor.execQuery(sql, prepared, new ResultHandler<List<Long>>() {
                @Override
                public List<Long> handle(ResultSet resultSet) throws SQLException {
                    List<Long> userList = new ArrayList<Long>();
                    while (resultSet.next()) {
                        userList.add(resultSet.getLong("user_id"));
                    }
                    return userList;
                }
            });
        } catch (SQLException sqlExc) {
            System.err.println("some troubles with sql=\n" + sql);
            System.err.println("?=" + chatId);
            sqlExc.printStackTrace();
        }
        return userList;
    }

    @Override
    public void addMessage(Long chatId, Message msg) {

        String sqlInsert = "INSERT INTO messages " +
                "(msg_text, chat_id, timestamp, user_id) VALUES " +
                "(?, ?, ?, ?) ";

        Map<Integer, Object> prepared = new HashMap<>();
        prepared.put(1, msg.getMessage());
        prepared.put(2, chatId);
        prepared.put(3, msg.getTimeStamp());
        prepared.put(4, msg.getSender());

        try {
            Long id = queryExecutor.updateQueryWithGeneratedKey(sqlInsert, prepared, "msg_id");
            msg.setMessageId(id);
        } catch (SQLException sqlExc) {
            System.err.println("troubles occured with parsign sql=\n");
            System.err.println(sqlInsert);
            System.err.println("?=" + prepared.get(1));
            System.err.println("?=" + prepared.get(2));
            System.err.println("?=" + prepared.get(3));
            System.err.println("?=" + prepared.get(4));
            sqlExc.printStackTrace();
        }
    }

    @Override
    public List<Long> getMessagesByChatId(Long chatId) {

        String sql = "SELECT msg_id " +
                "FROM messages " +
                "WHERE chat_id = ?";

        Map<Integer, Object> prepared = new HashMap<>();
        prepared.put(1, chatId);

        List<Long> msgList = null;
        try {
            msgList = queryExecutor.execQuery(sql, prepared, new ResultHandler<List<Long>>() {
                @Override
                public List<Long> handle(ResultSet resultSet) throws SQLException {
                    List<Long> msgList = new ArrayList<Long>();
                    while (resultSet.next()) {
                        msgList.add(resultSet.getLong("msg_id"));
                    }
                    return msgList;
                }
            });
        } catch (SQLException sqlExc) {
            System.err.println("some troubles with sql=\n" + sql);
            System.err.println("?=" + chatId);
            sqlExc.printStackTrace();
        }
        return msgList;

    }

    @Override
    public Message getMessageById(Long messageId) {

        String sql = "" +
                "SELECT * " +
                "FROM messages " +
                "WHERE msg_id = ?";

        Map<Integer, Object> prepared = new HashMap<>();
        prepared.put(1, messageId);
        Message msg = null;
        try {
            msg = queryExecutor.execQuery(sql, prepared, new ResultHandler<Message>() {
                @Override
                public Message handle(ResultSet resultSet) throws SQLException {
                    Message msg = new Message();
                    while (resultSet.next()) {
                        msg.setMessageId(resultSet.getLong("msg_id"));
                        msg.setMessage(resultSet.getString("msg_text"));
                        msg.setType(CommandType.CHAT_SEND);
                        msg.setSender(resultSet.getLong("user_id"));
                        msg.setTimeStamp(resultSet.getString("timestamp"));
                        return msg;
                    }
                    return null;
                }
            });
        } catch (SQLException sqlExc) {
            System.err.println("some troubles occured while parsing sql=\n" + sql);
            System.err.println("where ?=" + messageId);
            sqlExc.printStackTrace();
        }
        return msg;
    }

    @Override
    public User addUser(String userName, String password) throws Exception {

        User user;

        String sqlInsert = "INSERT INTO users (\"login\", \"password\") VAlUES " +
                "(?, ?);";

        Map<Integer, Object> prepared = new HashMap<>();
        prepared.put(1, userName);
        prepared.put(2, new String(password));

        Long id = queryExecutor.updateQueryWithGeneratedKey(sqlInsert, prepared, "id");

        user = new User(userName);
        user.setHash(password);
        user.setUserID(id);
        return user;

    }

}



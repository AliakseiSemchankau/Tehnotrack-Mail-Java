package ru.mail.track.session;

import ru.mail.track.message.messagetypes.Message;
import ru.mail.track.message.User;
import ru.mail.track.net.Server;
import ru.mail.track.net.io.ConnectionHandler;
import ru.mail.track.net.SessionManager;

/**
 * Класс содержит информацию о текущей сессии взаимодействия
 * Пока нам остаточно хранить юзера, возможно понадобится еще какое-то состояние
 */
public class Session {

    private Long id;
    private User sessionUser;
    //private ConnectionHandler connectionHandler;
    private SessionManager sessionManager;
    private Server server;

    public Session() {
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    //public ConnectionHandler getConnectionHandler() {
    //    return connectionHandler;
    //}

    //public void setConnectionHandler(ConnectionHandler connectionHandler) {
    //    this.connectionHandler = connectionHandler;
    //}

    public Session(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getSessionUser() {
        return sessionUser;
    }

    public void setSessionUser(User sessionUser) {
        this.sessionUser = sessionUser;
        sessionManager.registerUser(sessionUser.getUserID(), id);
    }

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void send(Message msg) throws Exception{

        //connectionHandler.send(msg);
        server.send(msg, this.id);

    }

}
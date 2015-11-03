package ru.mail.track;

import ru.mail.track.download.DownloadServiceFileImpl;
import ru.mail.track.message.MessageWorker;
import ru.mail.track.message.UserStorage;
import ru.mail.track.net.ThreadedServer;

/**
 * Created by aliakseisemchankau on 29.9.15.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        UserStorage store = new UserStorage("userinfo", new DownloadServiceFileImpl());
        store.initialize(new DownloadServiceFileImpl());
        //MessageStore messageStore = new Messag

        ThreadedServer server = new ThreadedServer(store,);
        server.startServer();


    }

}

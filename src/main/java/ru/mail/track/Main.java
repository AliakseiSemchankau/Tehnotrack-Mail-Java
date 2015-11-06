package ru.mail.track;

import ru.mail.track.data.DataService;
import ru.mail.track.data.DataServiceFileImpl;
import ru.mail.track.message.CommandHandler;
import ru.mail.track.message.MessageStore;
import ru.mail.track.message.MessageStoreStub;
import ru.mail.track.message.UserStorage;
import ru.mail.track.net.ThreadedServer;

/**
 * Created by aliakseisemchankau on 29.9.15.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        UserStorage store = new UserStorage();
        DataService dataService = new DataServiceFileImpl();

        store.initialize(dataService);

        MessageStore messageStore = new MessageStoreStub(dataService);

        ThreadedServer server = new ThreadedServer(store, new CommandHandler(store, messageStore));
        server.startServer();


    }

}

package ru.mail.track;

import ru.mail.track.data.DataService;
import ru.mail.track.data.DataServiceDBImpl;
import ru.mail.track.message.CommandHandler;
import ru.mail.track.message.MessageStore;
import ru.mail.track.message.MessageStoreStub;
import ru.mail.track.message.UserStorage;
import ru.mail.track.net.ThreadedServer;
import ru.mail.track.net.Server;

import java.io.InputStream;
import java.util.Scanner;

/**
 * Created by aliakseisemchankau on 29.9.15.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        UserStorage userStore = new UserStorage();
        DataService dataService = new DataServiceDBImpl();
        dataService.init();
        userStore.initialize(dataService);

        MessageStore messageStore = new MessageStoreStub(dataService);

        Server server = new ThreadedServer(new CommandHandler(userStore,  messageStore));

        Runnable r =  new Runnable() {

            @Override
            public void run() {
                try {
                    server.startServer();
                } catch (Exception e) {
                    System.err.println("can't start server(");
                    e.printStackTrace();
                }
            }
        };

        Thread thread = new Thread(r);
        thread.start();

        while(true) {
            System.out.println("here!");
            Scanner scanner = new Scanner(System.in);
            String query = scanner.next();
            if ("q".equals(query)) {
                server.destroyServer();
                thread.join();
                return;
            }
        }



    }

}

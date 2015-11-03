package ru.mail.track.net;

import ru.mail.track.message.AuthorizationService;
import ru.mail.track.message.MessageWorker;
import ru.mail.track.message.User;
import ru.mail.track.message.UserStorage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by aliakseisemchankau on 26.10.15.
 */
public class Server {

    public final int PORT = 19000;
    public UserStorage store;

    public Server(UserStorage store) {

        this.store = store;

    }

    public void work() {

        int clientID = 0;

        try (ServerSocket sSocket = new ServerSocket(PORT)) {

            while (true) {

                Socket socket = sSocket.accept();
                System.out.println("Accepted. " + socket.getInetAddress());
                ClientWorker clientWorker = new ClientWorker(socket, ++clientID);
                clientWorker.start();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class ClientWorker extends Thread {

        private int clientId;
        private Socket socket;

        ClientWorker(Socket socket, int clientId) {
            this.socket = socket;
            this.clientId = clientId;
        }

        @Override
        public void run() {

            try {
                //AuthorizationService authService = new AuthorizationService(store, );
                AuthorizationService authService = null;
                authService.start();

                if (authService.getSuccess()) {
                    User user = authService.getUser();
                    MessageWorker ms = new MessageWorker(user, store);
                    ms.start();
                }

                try (InputStream in = socket.getInputStream();
                     OutputStream out = socket.getOutputStream()) {

                    while (true) {
                        System.out.println("waiting");

                        byte[] buf = new byte[32 * 1024];
                        int readBytes = in.read(buf);
                        if (readBytes < 0) {
                            return;
                        }
                        String line = new String(buf, 0, readBytes);
                        //TODO: use here commandHandler instance to process input
                        // invoke commandHandler.perform()

                        // code below must be encapsulated in busines login (CommandHandler implementatino)
                        // 1 /login -> User
                        // 2 /message -> MeesageHandler.perform() :
                        // message {text, chatId,...}
                        // MessageStore.addMessage(message)

                        System.out.println("now client #" + clientId + " is printing message");
                        System.out.printf("Client>%s", line);

                        out.write(line.getBytes());
                        out.flush();
                    }
                } catch (IOException ioExc) {
                    System.err.println("can't get access for socket " + socket.toString());
                    ioExc.printStackTrace();
                }

            } catch (Exception exc) {
                System.err.println("some trouble with authService occured:(");
                System.err.println(exc.getMessage());
            }
        }
    }
}



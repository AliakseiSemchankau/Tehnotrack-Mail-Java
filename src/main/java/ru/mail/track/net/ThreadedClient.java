package ru.mail.track.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mail.track.message.messagetypes.HelpMessage;
import ru.mail.track.message.messagetypes.InfoMessage;
import ru.mail.track.message.messagetypes.LoginMessage;
import ru.mail.track.message.messagetypes.Message;
import ru.mail.track.message.messagetypes.RegisterMessage;
import ru.mail.track.message.messagetypes.SendMessage;
import ru.mail.track.message.messagetypes.SimpleMessage;
import ru.mail.track.perform.CommandType;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Клиентская часть
 */
public class ThreadedClient implements MessageListener {

    public static final int PORT = 19000;
    public static final String HOST = "localhost";

    static Logger log = LoggerFactory.getLogger(ThreadedClient.class);

    ConnectionHandler handler;

    public ThreadedClient() {
        try {
            Socket socket = new Socket(HOST, PORT);
            handler = new SocketConnectionHandler(socket);

            // Этот класс будет получать уведомления от socket handler
            handler.addListener(this);

            Thread socketHandler = new Thread(handler);
            socketHandler.start();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
            // exit, failed to open socket
        }
    }

    public void processInput(String line) throws IOException {
        String[] tokens = line.split(" ");
        log.info("Tokens: {}", Arrays.toString(tokens));
        String cmdType = tokens[0];



        if ("\\login".equals(cmdType)) {
            LoginMessage loginMessage = new LoginMessage();
            loginMessage.setType(CommandType.USER_LOGIN);
            loginMessage.setLogin(tokens[1]);
            loginMessage.setPassword(tokens[2]);
            handler.send(loginMessage);
            return;
        }

        if ("\\send".equals(cmdType)) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setType(CommandType.MSG_SEND);
            sendMessage.setChatId(Long.valueOf(tokens[1]));
            sendMessage.setMessage(tokens[2]);
            handler.send(sendMessage);
            return;
        }

        if ("\\simple".equals(cmdType)) {
            SimpleMessage simpleMessage = new SimpleMessage();
            simpleMessage.setType(CommandType.SIMPLE_MSG);
            simpleMessage.setMessage(tokens[1]);
            handler.send(simpleMessage);
            return;
        }

        if ("\\register".equals(cmdType)) {
            RegisterMessage registerMessage = new RegisterMessage();
            registerMessage.setType(CommandType.USER_REGISTER);
            registerMessage.setLogin(tokens[1]);
            registerMessage.setPassword(tokens[2]);
            handler.send(registerMessage);
            return;
        }

        if ("\\help".equals(cmdType)) {
            HelpMessage helpMessage = new HelpMessage();
            helpMessage.setType(CommandType.USER_HELP);
            handler.send(helpMessage);
            return;
        }

        if ("\\userinfo".equals(cmdType)) {
            InfoMessage infoMessage = new InfoMessage();
            infoMessage.setType(CommandType.USER_INFO);
            if (tokens.length > 1) {
                infoMessage.setHasArg(true);
                infoMessage.setUserId(Long.valueOf(tokens[1]));
            }
            handler.send(infoMessage);
            return;
        }

        System.out.println("Invalid input: " + line);

    }


    /**
     * Получено сообщение из handler, как обрабатывать
     */
    @Override
    public void onMessage(Message msg, long id) {
        System.out.printf("THREADED CLIENT: %s", msg.getMessage());
    }


    public static void main(String[] args) throws Exception {
        ThreadedClient client = new ThreadedClient();

        Scanner scanner = new Scanner(System.in);
        System.out.println("$");
        while (true) {
            String input = scanner.nextLine();
            //String input = scanner.next();
            //System.out.println("[" + input + "]");
            if ("q".equals(input)) {
                System.out.println("well, that's the end");
                return;
            }
            client.processInput(input);
        }
    }

}
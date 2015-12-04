package ru.mail.track.integr;


import org.junit.Before;
import org.junit.Test;
import ru.mail.track.data.DataService;
import ru.mail.track.data.DataServiceDBImpl;
import ru.mail.track.message.CommandHandler;
import ru.mail.track.message.MessageStore;
import ru.mail.track.message.MessageStoreStub;
import ru.mail.track.message.Result;
import ru.mail.track.message.UserStorage;
import ru.mail.track.message.messagetypes.Message;
import ru.mail.track.message.messagetypes.SimpleMessage;
import ru.mail.track.net.io.MessageListener;
import ru.mail.track.net.io.ThreadedClient;
import ru.mail.track.net.io.ThreadedServer;

/**
 * Created by aliakseisemchankau on 1.12.15.
 */
public class IntegrLogin implements MessageListener {

    ThreadedServer server;
    ThreadedClient client1;
    String result;

    @Before
    public void setup() throws Exception{

        UserStorage userStore = new UserStorage();
        DataService dataService = new DataServiceDBImpl();
        dataService.init();
        userStore.initialize(dataService);

        MessageStore messageStore = new MessageStoreStub(dataService);

        server = new ThreadedServer(new CommandHandler(userStore,  messageStore));

        try {
            new Thread(() -> {
                server.startServer();
            }).start();
        } catch (Exception exc) {
            exc.printStackTrace();
            System.err.println("some troubles with starting server(");
        }

        Thread.sleep(100);

        client1 = new ThreadedClient();


        client1.getHandler().addListener(this);

    }

    @Test
    public void login() throws Exception {
        Result expectedResult1 = new Result(true, "", "user with login=aliaksei" + " have authorized succesfully");
        SimpleMessage msg1 = new SimpleMessage();
        msg1.setMessage(expectedResult1.toString());
//        assert/*that*/(gotResult(new SendMessage(-1L, "Login Ok"),/*on request*/"login A 1"));
        assert/*that*/(gotResult(msg1.toString(),/*on request*/"\\login aliaksei 123"));

        Result expectedResult2 = new Result(false, "user with userName=aliakseii" + " doesn't exist");
        SimpleMessage msg2 = new SimpleMessage();
        msg2.setMessage(expectedResult2.toString());
//        assert/*that*/(gotResult(new SendMessage(-1L, "Login Ok"),/*on request*/"login A 1"));
        assert/*that*/(gotResult(msg2.toString(),/*on request*/"\\login aliakseii 123"));

        Result expectedResult3 = new Result(false, "login or password is incorrect");
        SimpleMessage msg3 = new SimpleMessage();
        msg3.setMessage(expectedResult3.toString());
//        assert/*that*/(gotResult(new SendMessage(-1L, "Login Ok"),/*on request*/"login A 1"));
        assert/*that*/(gotResult(msg3.toString(),/*on request*/"\\login aliaksei 1243"));

    }

    private boolean gotResult(Object result, String on) throws Exception {
        client1.processInput(on);
        Thread.sleep(500);
        return this.result.equals(result.toString());
    }

    @Override
    public void onMessage(Message message, long id) {

        result = message.toString();

       // System.out.println(result + "=result");
    }
}

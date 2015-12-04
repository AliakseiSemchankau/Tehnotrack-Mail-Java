package ru.mail.track.net.nio;



import ru.mail.track.message.CommandHandler;
import ru.mail.track.message.messagetypes.Message;

import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.List;

/**
 *
 */
public class EchoWorker implements Runnable {
    private List queue = new LinkedList();
    private CommandHandler commandHandler;

    public void setCommandHandler(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    public void processData(NioServer server, SocketChannel socket, byte[] data, int count) {
        //Message msg =
        byte[] kek = "kek".getBytes();
        byte[] dataCopy = new byte[count + kek.length];

        System.arraycopy(data, 0, dataCopy, 0, count);
        for(int i = count; i < count + kek.length; ++i) {
            dataCopy[i] = kek[i - count];
        }
        synchronized(queue) {
            queue.add(new ServerDataEvent(server, socket, dataCopy));
            queue.notify();
        }
    }

    public void run() {
        ServerDataEvent dataEvent;

        while(true) {
            // Wait for data to become available
            synchronized(queue) {
                while(queue.isEmpty()) {
                    try {
                        queue.wait();
                    } catch (InterruptedException e) {
                    }
                }
                dataEvent = (ServerDataEvent) queue.remove(0);
            }

            // Return to sender
            System.out.println("going to send:" + new String(dataEvent.data));
            dataEvent.server.send(dataEvent.socket, dataEvent.data);
        }
    }
}
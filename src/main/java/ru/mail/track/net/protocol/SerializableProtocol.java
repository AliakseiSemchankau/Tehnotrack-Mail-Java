package ru.mail.track.net.protocol;

import ru.mail.track.message.messagetypes.Message;
import ru.mail.track.message.messagetypes.SimpleMessage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by aliakseisemchankau on 10.11.15.
 */
public class SerializableProtocol implements Protocol {
    @Override
    public Message decode(byte[] bytes) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            Message msg = (Message) ois.readObject();
            return msg;
        } catch (Exception e) {

        }
        return new SimpleMessage("some mistake occured");
    }

    @Override
    public byte[] encode(Message msg) {

        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(msg);
            byte[] encodedMsg = bos.toByteArray();
            return encodedMsg;
        } catch (IOException ioExc) {

        }
        return new byte[0];
    }
}

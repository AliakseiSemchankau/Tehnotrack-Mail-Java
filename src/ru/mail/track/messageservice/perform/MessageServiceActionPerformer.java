package ru.mail.track.messageservice.perform;

import ru.mail.track.messageservice.MessageService;
import sun.plugin2.message.Message;

/**
 * Created by aliakseisemchankau on 15.10.15.
 */
public abstract class MessageServiceActionPerformer {

    public abstract void perform(String[] cmd, MessageService ms);

}

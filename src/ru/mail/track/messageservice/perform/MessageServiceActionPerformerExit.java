package ru.mail.track.messageservice.perform;

import ru.mail.track.messageservice.MessageService;

/**
 * Created by aliakseisemchankau on 15.10.15.
 */
public class MessageServiceActionPerformerExit extends MessageServiceActionPerformer {
    @Override
    public void perform(String[] cmd, MessageService ms) {
        ms.mustExit();
    }
}

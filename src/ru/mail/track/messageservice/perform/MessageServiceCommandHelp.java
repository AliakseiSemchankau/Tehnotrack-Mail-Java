package ru.mail.track.messageservice.perform;

import ru.mail.track.messageservice.MessageService;

/**
 * Created by aliakseisemchankau on 15.10.15.
 */
public class MessageServiceCommandHelp extends MessageServiceCommand {
    @Override
    public void perform(String[] cmd, MessageService ms) {
        System.out.println("\\help\n\\user\n\\history\n\\find\n\\exit");
    }
}

package ru.mail.track.messageservice.perform;

import ru.mail.track.messageservice.MessageService;

/**
 * Created by aliakseisemchankau on 15.10.15.
 */
public class MessageServiceActionPerformerUser extends MessageServiceActionPerformer {
    @Override
    public void perform(String[] cmd, MessageService ms) {
        if (cmd.length < 2) {
            System.out.println("you forgot to add the new alias");
            return;
        }
        String newNickName = cmd[1];
        System.out.println("your old nickname is " + ms.getNickName());
        ms.setNickName(newNickName);
        System.out.println("your new nickname is " + ms.getNickName());
    }
}

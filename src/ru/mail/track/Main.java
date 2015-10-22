package ru.mail.track;

import ru.mail.track.download.DownloadService;
import ru.mail.track.download.DownloadServiceFileImpl;
import ru.mail.track.messageservice.MessageService;

/**
 * Created by aliakseisemchankau on 29.9.15.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        UserStorage store = new UserStorage("userinfo", new DownloadServiceFileImpl() {
        });
        store.open();
        AuthorizationService authService = new AuthorizationService(store);
        authService.start();

        if (authService.getSuccess()) {
            User user = authService.getUser();
            MessageService ms = new MessageService(user, store);
            ms.start();
        }

        store.close();
    }

}

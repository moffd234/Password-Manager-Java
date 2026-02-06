package com.moffd.app.Console;

import com.moffd.app.Models.UserSession;
import com.moffd.app.Utils.IOConsole;

import java.util.List;

public class MenuConsole {
    UserSession session;
    IOConsole console;

    public MenuConsole(UserSession session, IOConsole console) {
        this.session = session;
        this.console = console;
    }

    public void run() {
        String choice = console.getValidInput("[ credentials ], [ account ], [ logout ]",
                List.of("credentials", "account", "logout"));

        switch (choice) {
            case "credentials" -> new CredentialConsole(session, console).run();
            case "account" -> new AccountConsole(session, console).run();
            case "logout" -> {
            }
        }
    }
}

package com.moffd.app.Console;

import com.moffd.app.Models.User;
import com.moffd.app.Utils.IOConsole;

import java.util.List;

public class MenuConsole {
    User currentUser;
    IOConsole console;

    public MenuConsole(User currentUser, IOConsole console) {
        this.currentUser = currentUser;
        this.console = console;
    }

    public void run() {
        String choice = console.getValidInput("[ credentials ], [ account ], [ logout ]",
                List.of("credentials", "account", "logout"));

        switch (choice) {
            case "credentials" -> new CredentialConsole(currentUser, console).run();
            case "account" -> new AccountConsole(currentUser, console).run();
            case "logout" -> {
                return;
            }
        }
    }
}

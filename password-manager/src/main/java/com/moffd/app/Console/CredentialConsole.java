package com.moffd.app.Console;

import com.moffd.app.Models.User;
import com.moffd.app.Utils.IOConsole;

public class CredentialConsole {
    private User currentUser;
    private IOConsole console;

    public CredentialConsole(User currentUser, IOConsole console) {
        this.currentUser = currentUser;
        this.console = console;
    }

    public void run() {
    }
}

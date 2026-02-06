package com.moffd.app.Console;

import com.moffd.app.Models.UserSession;
import com.moffd.app.Utils.IOConsole;

public class AccountConsole {
    UserSession session;
    IOConsole console;

    public AccountConsole(UserSession session, IOConsole console) {
        this.session = session;
        this.console = console;
    }

    public void run() {
    }
}

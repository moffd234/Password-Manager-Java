package com.moffd.app.Console;

import com.moffd.app.Models.User;
import com.moffd.app.Utils.IOConsole;

public class MenuConsole {
    User currentUser;
    IOConsole console;

    public MenuConsole(IOConsole console) {
        this.console = console;
    }
}

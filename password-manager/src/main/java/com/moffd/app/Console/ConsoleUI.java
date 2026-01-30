package com.moffd.app.Console;

import com.moffd.app.Models.User;
import com.moffd.app.Utils.IOConsole;

import java.util.List;

public class ConsoleUI {
    private final IOConsole ioConsole;
    private User currentUser;

    public ConsoleUI(IOConsole ioConsole) {
        this.ioConsole = ioConsole;
    }

}

package com.moffd.app;

import com.moffd.app.Console.AuthConsole;
import com.moffd.app.Console.MenuConsole;
import com.moffd.app.Models.User;
import com.moffd.app.Utils.IOConsole;

/**
 * Application entry point
 */
public class App {
    public static void main(String[] args) {
        IOConsole console = new IOConsole();
        AuthConsole authConsole = new AuthConsole(console);

        while (true) {
            User user = authConsole.authenticate();

            if (user != null) {
                new MenuConsole(user, console).run();
            }
        }
    }
}

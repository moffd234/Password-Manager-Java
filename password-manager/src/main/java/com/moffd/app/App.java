package com.moffd.app;

import com.moffd.app.Console.AuthConsole;
import com.moffd.app.Console.MenuConsole;
import com.moffd.app.Models.UserSession;
import com.moffd.app.Utils.IOConsole;

/**
 * Application entry point
 */
public class App {
    public static void main(String[] args) {
        IOConsole console = new IOConsole();
        AuthConsole authConsole = new AuthConsole(console);

        while (true) {
            UserSession session = authConsole.authenticate();

            if (session != null) {
                new MenuConsole(session, console).run();
            }
        }
    }
}

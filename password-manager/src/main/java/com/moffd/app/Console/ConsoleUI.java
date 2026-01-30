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

    private User authUser(){
        User user = null;

        while (user == null) {
            String loginChoice = ioConsole.getValidInput("[ login ] [ signup ] [ exit ]",
                    List.of("login", "signup", "exit"));

            if (loginChoice.equals("login")) {
                user = login();
            } else if(loginChoice.equals("signup")){
                user = signUp();
            } else{
                return null;
            }
        }
        return user;
    }

    private User login() {
        return null;
    }

    private User signUp() {
        return null;
    }

}

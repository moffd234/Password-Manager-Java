package com.moffd.app.Console;

import com.moffd.app.Dao.UserDao;
import com.moffd.app.Models.User;
import com.moffd.app.Utils.IOConsole;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.util.List;

public class ConsoleUI {
    private final IOConsole ioConsole;
    private User currentUser;
    private final UserDao userDao = new UserDao();

    public ConsoleUI(IOConsole ioConsole) {
        this.ioConsole = ioConsole;
    }

    private User authUser() {
        User user = null;

        while (user == null) {
            String loginChoice = ioConsole.getValidInput("[ login ] [ signup ] [ exit ]",
                    List.of("login", "signup", "exit"));

            if (loginChoice.equals("login")) {
                user = login();
            } else if (loginChoice.equals("signup")) {
                user = signUp();
            } else {
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

    /**
     * Checks if a given password meets the following requirements
     * - At least 26 character
     * - At least one digit
     * - At least one special character (@#$%^&+=!)
     *
     * @param password The string to check
     * @return null if valid. Otherwise, returns a description of why its invalid
     */
    public String validatePassword(String password) {
        if (password.length() < 26) {
            return "Password must be at least 26 characters long.";
        }
        if (!password.matches(".*[A-Z].*")) {
            return "Password must contain at least one uppercase letter.";
        }
        if (!password.matches(".*[a-z].*")) {
            return "Password must contain at least one lowercase letter.";
        }
        if (!password.matches(".*\\d.*")) {
            return "Password must contain at least one digit.";
        }
        if (!password.matches(".*[@#$%^&+=!].*")) {
            return "Password must contain at least one special character (@#$%^&+=!).";
        }
        return null;
    }

    /**
     * Checks if a given email meets valid email formatting
     *
     * @param email string to check for valid email format
     * @return null if valid. Otherwise, returns a description of why its invalid
     */
    public String validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            return "Email cannot be empty";
        }

        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return !email.matches(regex) ? "Email is not a valid format" : null;
    }

    private String hashPassword(String password) {
        int logRounds = 12;

        String salt = BCrypt.gensalt(logRounds);

        return BCrypt.hashpw(password, salt);
    }

    private boolean checkPassword(String enteredPwd, String hashedPwd) {
        return BCrypt.checkpw(enteredPwd, hashedPwd);
    }

    private String getValidUsername() {
        String username = ioConsole.getStringInput("Please create a username").trim();

        try{

            while(userDao.findByUsername(username) != null || username.trim().isEmpty()){
                ioConsole.printError("Username already taken");
                username = ioConsole.getStringInput("Please create a username");
            }

        } catch (SQLException e) {
            ioConsole.printError("Issue creating username. Please try again later");
            return null;
        }

        return username;
    }

}

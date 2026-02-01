package com.moffd.app.Console;

import com.moffd.app.Dao.UserDao;
import com.moffd.app.Models.User;
import com.moffd.app.Utils.IOConsole;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CancellationException;

public class ConsoleUI {
    private final IOConsole ioConsole;
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

        try {
            String username = requireField(getValidUsername());
            String password = requireField(getValidPassword());
            String email = requireField(getValidEmail());

            String hashedPassword = hashPassword(password);
            User tempUser = new User(0, username, hashedPassword, email);

            return userDao.create(tempUser);

        } catch (CancellationException e) {
            return null;
        } catch (SQLException e) {
            ioConsole.printError("Issue creating new user.");
            return null;
        }
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
        while (true) {
            String username = ioConsole.getStringInput("Please create a username").trim();

            if (username.equalsIgnoreCase("back")) {
                return null;
            }

            if (username.isEmpty()) {
                ioConsole.printError("Username cannot be empty");
                continue;
            }

            try {
                if (userDao.findByUsername(username) == null) {
                    return username;
                }

                ioConsole.printError("Username is already taken");

            } catch (SQLException e) {
                ioConsole.printError("Issue creating username. Please try again late");
                return null;
            }
        }
    }

    private String getValidPassword() {
        while (true) {
            String password = ioConsole.getStringInput("Please create a password ");

            if (password.equalsIgnoreCase("back")) {
                return null;
            }

            String message = validatePassword(password);

            if (message != null) {
                ioConsole.printError(message);
                continue;
            }

            String confirmation = ioConsole.getStringInput("Confirm your password");

            if (confirmation.equalsIgnoreCase("back")) {
                return null;
            }

            if (!confirmation.equals(password)) {
                ioConsole.printError("Passwords do not match");
                continue;
            }

            return password;
        }
    }

    private String getValidEmail() {
        while (true) {
            String email = ioConsole.getStringInput("Please create an email ").trim();

            if (email.equalsIgnoreCase("back")) {
                return null;
            }

            String message = validateEmail(email);

            if (message != null) {
                ioConsole.printError(message);
                continue;
            }

            return email;
        }
    }

    private String requireField(String input) {

        if (input == null) {
            throw new CancellationException();
        }

        return input;
    }
}

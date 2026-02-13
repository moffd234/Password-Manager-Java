package com.moffd.app.Console;

import com.moffd.app.Dao.UserDao;
import com.moffd.app.Models.User;
import com.moffd.app.Models.UserSession;
import com.moffd.app.Utils.CryptoService;
import com.moffd.app.Utils.IOConsole;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CancellationException;

import static com.moffd.app.Utils.InputValidator.validateEmail;
import static com.moffd.app.Utils.InputValidator.validatePassword;
import static com.moffd.app.Utils.RequireInput.requireField;

public class AuthConsole {
    private final IOConsole ioConsole;
    private final UserDao userDao;
    private final CryptoService cryptoService;

    public AuthConsole(IOConsole ioConsole) {
        this.ioConsole = ioConsole;
        this.userDao = new UserDao();
        this.cryptoService = new CryptoService();
    }

    /**
     * Runs main authentication loop prompting the user to login or signup. The method will keep prompting until
     * a valid {@link UserSession} is returned. Note: User can also type 'exit' at any time to exit application.
     * @return a new {@link UserSession} containing the created user and their secret key
     */
    public UserSession authenticate() {
        UserSession session = null;

        while (session == null) {
            String loginChoice = ioConsole.getValidInput("[ login ] [ signup ] [ exit ]",
                    List.of("login", "signup", "exit"));

            if (loginChoice.equals("login")) {
                session = login();
            } else if (loginChoice.equals("signup")) {
                session = signUp();
            }
        }

        return session;
    }

    private UserSession login() {
        for (int i = 0; i < 5; i++) {

            try {
                String username = requireField(ioConsole.getStringInput("Please enter your username").trim());
                String password = requireField(ioConsole.getStringInput("Please enter your password"));

                User userAccount = userDao.findByUsername(username);

                if (userAccount == null || !cryptoService.checkPassword(password, userAccount.getMasterPassword())) {
                    ioConsole.printError("Incorrect username or password");
                    continue;
                }

                return new UserSession(userAccount, cryptoService.getKeyFromPassword(password, userAccount.getSalt()));

            } catch (CancellationException e) {
                return null;
            } catch (SQLException | NoSuchAlgorithmException | InvalidKeySpecException e) {
                ioConsole.printError("Error finding account with that username please try again");
            }
        }

        ioConsole.printError("Too many login attempts please try again later.");
        return null;
    }

    private UserSession signUp() {

        try {
            String username = requireField(getValidUsername());
            String password = requireField(getValidPassword());
            String email = requireField(getValidEmail());

            String hashedPassword = cryptoService.hashPassword(password);
            byte[] salt = cryptoService.generateSalt();

            User tempUser = new User(0, username, hashedPassword, email, salt);

            return new UserSession(userDao.create(tempUser), cryptoService.getKeyFromPassword(password, salt));

        } catch (CancellationException e) {
            return null;
        } catch (SQLException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            ioConsole.printError("Issue creating new user.");
            return null;
        }
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

            try {
                if (userDao.findByEmail(email) == null) {
                    return email;
                }

                ioConsole.printError("email is already taken");

            } catch (SQLException e) {
                ioConsole.printError("Issue creating email. Please try again late");
                return null;
            }
        }
    }

}

package com.moffd.app.Console;

import com.moffd.app.Dao.UserDao;
import com.moffd.app.Models.User;
import com.moffd.app.Models.UserSession;
import com.moffd.app.Utils.IOConsole;
import org.mindrot.jbcrypt.BCrypt;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CancellationException;

import static com.moffd.app.Utils.InputValidator.validateEmail;
import static com.moffd.app.Utils.InputValidator.validatePassword;
import static java.lang.System.exit;

public class AuthConsole {
    private final IOConsole ioConsole;
    private final UserDao userDao = new UserDao();

    public AuthConsole(IOConsole ioConsole) {
        this.ioConsole = ioConsole;
    }

    public User authenticate() {
        User user = null;

        while (user == null) {
            String loginChoice = ioConsole.getValidInput("[ login ] [ signup ] [ exit ]",
                    List.of("login", "signup", "exit"));

            if (loginChoice.equals("login")) {
                user = login();
            } else if (loginChoice.equals("signup")) {
                user = signUp();
            } else {
                exit(0);
            }
        }

        return user;
    }

    private User login() {
        for (int i = 0; i < 5; i++) {

            try {
                String username = requireField(ioConsole.getStringInput("Please enter your username").trim());
                String password = requireField(ioConsole.getStringInput("Please enter your password"));

                User userAccount = userDao.findByUsername(username);

                if (userAccount == null || !checkPassword(password, userAccount.getMasterPassword())) {
                    ioConsole.printError("Incorrect username or password");
                    continue;
                }

                return userAccount;

            } catch (CancellationException e) {
                return null;
            } catch (SQLException e) {
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

            String hashedPassword = hashPassword(password);
            byte[] salt = getSalt();

            User tempUser = new User(0, username, hashedPassword, email, salt);

            return new UserSession(userDao.create(tempUser), getKeyFromPassword(password, salt));

        } catch (CancellationException e) {
            return null;
        } catch (SQLException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            ioConsole.printError("Issue creating new user.");
            return null;
        }
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

    private String requireField(String input) throws CancellationException {

        if (input == null || input.equalsIgnoreCase("back")) {
            throw new CancellationException();
        }

        return input;
    }

    private byte[] getSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return (salt);
    }

    private static SecretKey getKeyFromPassword(String password, byte[] salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);

        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
    }

}

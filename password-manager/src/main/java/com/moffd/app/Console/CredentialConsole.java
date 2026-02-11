package com.moffd.app.Console;

import com.moffd.app.Dao.CredentialDao;
import com.moffd.app.Models.Credential;
import com.moffd.app.Models.CredentialInfo;
import com.moffd.app.Models.UserSession;
import com.moffd.app.Utils.AnsiColor;
import com.moffd.app.Utils.IOConsole;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.CancellationException;

import static com.moffd.app.Utils.RequireInput.requireField;

public class CredentialConsole {
    private final UserSession session;
    private final IOConsole console;
    private final CredentialDao credentialDao;

    public CredentialConsole(UserSession session, IOConsole console) {
        this.session = session;
        this.console = console;
        this.credentialDao = new CredentialDao();
    }

    public void run() {
    }

    private Credential insertNewCredential() {
        Credential cred = getCredential();

        if (cred == null) {
            return null;
        }

        try {
            return credentialDao.create(cred);
        } catch (SQLException e) {
            console.printError("Error creating credential for site " + cred.getSite());
            return null;
        }
    }

    private void printCredentialList() {
        try {
            List<Credential> credentials = credentialDao.findAllForUser(session.getUser());

            if (credentials.isEmpty()) {
                console.printError("No credentials found");
            }

            for (Credential cred : credentials) {
                console.println("Site: " + cred.getSite() + " | Username: " + cred.getSiteUsername());
            }

            console.println("To view a password please search for a specific credentials");

        } catch (SQLException e) {
            console.printError("Error fetching all credentials");
        }
    }

    private void deleteCredential() {
        String siteName = requireField(console.getStringInput("Enter site the credential is tied to"));
        String username = requireField(console.getStringInput("Enter username for credential"));

        try {
            List<Credential> matchingCreds = credentialDao.findBySiteAndUsername(session.getUser().getId(), siteName, username);

            if (matchingCreds.isEmpty()) {
                console.println("No matching credentials found.");
                return;
            }

            boolean deleteAll = console.getYesNoInput(matchingCreds.size() +
                    " matching credentials found. Would you like to delete all of them?");

            if (deleteAll) {
                for (Credential cred : matchingCreds) {
                    credentialDao.delete(cred.getId());
                }

                console.printlnColored("Successfully deleted credentials", AnsiColor.GREEN);

            } else {
                for (Credential cred : matchingCreds) {

                    console.println("Site: " + cred.getSite());
                    console.println("Username: " + cred.getSiteUsername());
                    console.println("Password: " + decrypt(cred.getSitePassword(), session.getEncryptionKey(), cred.getIv()));
                    boolean deleteCred = console.getYesNoInput("Delete this credential?");

                    if (deleteCred) {
                        deleteIndividualCred(cred);
                    }
                }
            }


        } catch (SQLException e) {
            console.printError("Issue handling credentials. Please try again later");
        } catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException | BadPaddingException |
                 InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException e) {
            console.printError("Issue decrypting password. Please try again later");
        }

    }

    private void deleteIndividualCred(Credential cred) {
        try{
            credentialDao.delete(cred.getId());
            console.printlnColored("Successfully deleted credential", AnsiColor.GREEN);
        } catch (SQLException e) {
            console.printError("Error deleting the following credential:" +
                    "\nSite: " + cred.getSite() +
                    "\nUsername " + cred.getSiteUsername());
        }
    }

    private CredentialInfo getCredentialInfo() {
        while (true) {
            try {
                String username = requireField(console.getStringInput("Please enter username"));
                String siteName = requireField(console.getStringInput("Please enter name of website / service"));
                String password = getConfirmedPassword();

                boolean isCorrect = console.getYesNoInput("You entered:\n" +
                        "Username: " + username +
                        "\nSite: " + siteName +
                        "\nIs this correct (y/n)");

                if (isCorrect) {
                    return new CredentialInfo(siteName, username, password);
                }


            } catch (CancellationException e) {
                return null;
            }
        }
    }

    private Credential getCredential() {
        CredentialInfo credentialInfo = getCredentialInfo();

        if (credentialInfo == null) {
            return null;
        }

        try {
            byte[] iv = generateIv();
            String encrypted = encrypt(credentialInfo.password(), session.getEncryptionKey(), iv);
            return new Credential(0, session.getUser().getId(), credentialInfo.site(), credentialInfo.username(), encrypted, iv);

        } catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchPaddingException |
                 IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException e) {

            console.printError("Error creating credential please try again later");
            return null;
        }
    }

    private String getConfirmedPassword() throws CancellationException {
        String password = requireField(console.getStringInput("Please enter password"));
        String confPassword = requireField(console.getStringInput("Please confirm password"));

        while (!confPassword.equals(password)) {
            console.printError("Passwords do not match");
            password = requireField(console.getStringInput("Please enter password"));
            confPassword = requireField(console.getStringInput("Please confirm password"));
        }

        return password;
    }

    private byte[] generateIv() {
        byte[] iv = new byte[12];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    private String encrypt(String input, SecretKey key, byte[] iv)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {

        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

        cipher.init(Cipher.ENCRYPT_MODE, key, gcmParameterSpec);
        byte[] cipherText = cipher.doFinal(input.getBytes());

        return Base64.getEncoder().encodeToString(cipherText);
    }

    private String decrypt(String cipherText, SecretKey key, byte[] iv)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {

        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

        cipher.init(Cipher.DECRYPT_MODE, key, gcmParameterSpec);
        byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText));

        return new String(plainText);
    }
}

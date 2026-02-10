package com.moffd.app.Console;

import com.moffd.app.Dao.CredentialDao;
import com.moffd.app.Models.Credential;
import com.moffd.app.Models.UserSession;
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

    private byte[] generateIv() {
        byte[] iv = new byte[12];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    private String encrypt(String algorithm, String input, SecretKey key, byte[] iv)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {

        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv);
        Cipher cipher = Cipher.getInstance(algorithm);

        cipher.init(Cipher.ENCRYPT_MODE, key, gcmParameterSpec);
        byte[] cipherText = cipher.doFinal(input.getBytes());

        return Base64.getEncoder().encodeToString(cipherText);
    }

    private String decrypt(String algo, String cipherText, SecretKey key, byte[] iv)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {

        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv);
        Cipher cipher = Cipher.getInstance(algo);

        cipher.init(Cipher.DECRYPT_MODE, key, gcmParameterSpec);
        byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText));

        return new String(plainText);
    }
}

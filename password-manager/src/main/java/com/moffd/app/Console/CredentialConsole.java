package com.moffd.app.Console;

import com.moffd.app.Dao.CredentialDao;
import com.moffd.app.Models.UserSession;
import com.moffd.app.Utils.IOConsole;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

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

    private GCMParameterSpec generateIv() {
        byte[] iv = new byte[12];
        new SecureRandom().nextBytes(iv);
        return new GCMParameterSpec(128, iv);
    }

    private String encrypt(String algorithm, String input, SecretKey key, GCMParameterSpec iv)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] cipherText = cipher.doFinal(input.getBytes());

        return Base64.getEncoder().encodeToString(cipherText);
    }
}

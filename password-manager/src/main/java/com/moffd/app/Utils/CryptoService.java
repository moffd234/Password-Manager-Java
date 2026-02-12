package com.moffd.app.Utils;

import org.mindrot.jbcrypt.BCrypt;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class CryptoService {

    public SecretKey getKeyFromPassword(String password, byte[] salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);

        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
    }

    public byte[] getSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return (salt);
    }

    public byte[] generateIv() {
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

    private String hashPassword(String password) {
        int logRounds = 12;

        String salt = BCrypt.gensalt(logRounds);

        return BCrypt.hashpw(password, salt);
    }

    private boolean checkPassword(String enteredPwd, String hashedPwd) {
        return BCrypt.checkpw(enteredPwd, hashedPwd);
    }

}

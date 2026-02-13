package com.moffd.app.Utils;

import org.mindrot.jbcrypt.BCrypt;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class CryptoService {

    /**
     * Generates am AES {@link SecretKey} based on a user's password and salt using PBKDF2
     * @param password User's plaintext password
     * @param salt a secure random salt
     * @return an AES {@link SecretKey} derived from the user's password and salt
     * @throws NoSuchAlgorithmException if PBKDF2WithHmacSHA256 is not available
     * @throws InvalidKeySpecException if the key spec is invalid
     */
    public SecretKey getKeyFromPassword(String password, byte[] salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);

        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
    }

    /**
     * Generates a secure random 16 byte salt
     * @return a secure random 16 byte salt
     */
    public byte[] generateSalt() {
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    /**
     * Generates a secure random 12 byte initialization vector
     * @return a random 12 byte array representing an initialization vector
     */
    public byte[] generateIv() {
        byte[] iv = new byte[12];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    /**
     * Takes a plaintext input and returns an encrypted output using AES in GCM mode with no padding
     *
     * @param input The plaintext string to encrypt
     * @param key The user's {@link SecretKey}
     * @param iv The credential's unique initialization vector
     * @return The Base64 encoded cipherText
     */
    public String encrypt(String input, SecretKey key, byte[] iv)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {

        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

        cipher.init(Cipher.ENCRYPT_MODE, key, gcmParameterSpec);
        byte[] cipherText = cipher.doFinal(input.getBytes(StandardCharsets.UTF_8));

        return Base64.getEncoder().encodeToString(cipherText);
    }

    /**
     * Decrypts a Base64 encoded cipherText using AES in GCM mode with no padding
     *
     * @param cipherText The encrypted cipherText to decrypt
     * @param key The user's {@link SecretKey}
     * @param iv The credential's unique initialization value
     */
    public String decrypt(String cipherText, SecretKey key, byte[] iv)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {

        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, iv);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

        cipher.init(Cipher.DECRYPT_MODE, key, gcmParameterSpec);
        byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText));

        return new String(plainText, StandardCharsets.UTF_8);
    }

    /**
     * Hashes a plaintext password using Bcrypt with 12 cost factor
     * @param password The plaintext password to hash
     * @return The hashed password
     */
    public String hashPassword(String password) {
        int logRounds = 12;

        String salt = BCrypt.gensalt(logRounds);

        return BCrypt.hashpw(password, salt);
    }

    /**
     * Checks if a given password against a stored Bcrypt hash
     * @param enteredPwd The plaintext password the user inputted
     * @param hashedPwd The stored Bcrypt hash
     * @return True if the password matches. False otherwise.
     */
    public boolean checkPassword(String enteredPwd, String hashedPwd) {
        return BCrypt.checkpw(enteredPwd, hashedPwd);
    }

}

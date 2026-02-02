package com.moffd.app.Utils;

public class InputValidator {
    /**
     * Checks if a given password meets the following requirements
     * - At least 26 character
     * - At least one digit
     * - At least one special character (@#$%^&+=!)
     *
     * @param password The string to check
     * @return null if valid. Otherwise, returns a description of why its invalid
     */
    public static String validatePassword(String password) {
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
    public static String validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            return "Email cannot be empty";
        }

        String regex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return !email.matches(regex) ? "Email is not a valid format" : null;
    }
}

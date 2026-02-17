package com.moffd.app.Utils;

import java.util.concurrent.CancellationException;

public class RequireInput {

    /**
     * Takes an input string and throws a cancellation exception if the input is null or 'back' (case ignored)
     * @param input The string to check for null or 'back'
     * @return the inputted string if field is valid. Otherwise, throws a cancellation exception.
     * @throws CancellationException if input is null or back
     */
    public static String requireField(String input) throws CancellationException {

        if (input == null || input.equalsIgnoreCase("back")) {
            throw new CancellationException();
        }

        return input;
    }
}

package com.moffd.app.Utils;

import java.util.concurrent.CancellationException;

public class RequireInput {
    public static String requireField(String input) throws CancellationException {

        if (input == null || input.equalsIgnoreCase("back")) {
            throw new CancellationException();
        }

        return input;
    }
}

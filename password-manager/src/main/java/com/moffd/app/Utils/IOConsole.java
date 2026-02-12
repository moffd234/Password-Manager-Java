package com.moffd.app.Utils;

import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;

public class IOConsole {
    private final Scanner input = new Scanner(System.in);
    private final PrintStream output = System.out;
    private final AnsiColor color;

    public IOConsole() {
        color = AnsiColor.AUTO;
    }

    public IOConsole(AnsiColor color) {
        this.color = color;
    }

    public void print(String prompt) {
        output.print(color.getColor() + prompt);
    }

    public void printColored(String prompt, AnsiColor color) {
        output.print(color.getColor() + prompt);
    }

    public void println(String prompt) {
        output.println(color.getColor() + prompt + "\n");
    }

    public void printlnColored(String prompt, AnsiColor color) {
        output.println(color.getColor() + prompt + "\n" + AnsiColor.AUTO.getColor());
    }

    public void printError(String prompt) {
        output.println(AnsiColor.RED.getColor() + prompt + "\n");
    }

    public String getStringInput(String prompt) {
        this.println(prompt);

        String response = input.nextLine();
        this.checkForExit(response);

        return response;
    }

    public Double getDoubleInput(String prompt) {
        String response = this.getStringInput(prompt);

        try {
            return Double.valueOf(response);
        } catch (NumberFormatException e) {
            this.printError("Response: " + response + " is not a valid number ");
        }

        return getDoubleInput(prompt);
    }

    public Integer getIntInput(String prompt) {
        return getDoubleInput(prompt).intValue();
    }

    public boolean getYesNoInput(String prompt) {
        String result = this.getStringInput(prompt);

        if (result.equalsIgnoreCase("yes") || result.equalsIgnoreCase("y")) {
            return true;
        } else if (result.equalsIgnoreCase("no") || result.equalsIgnoreCase("n")) {
            return false;
        }

        return getYesNoInput(prompt);
    }

    public void checkForExit(String response) {
        if (response.equalsIgnoreCase("exit")) {
            printColored("Exiting application", AnsiColor.RED);
            System.exit(0);
        }
    }

    public String getValidInput(String prompt, List<String> validOptions) {
        String input = getStringInput(prompt).toLowerCase().trim();

        while (!validOptions.contains(input)) {
            printError("Invalid option. Valid options are: " + validOptions);
            input = getStringInput(prompt).toLowerCase().trim();
        }

        return input;
    }
}

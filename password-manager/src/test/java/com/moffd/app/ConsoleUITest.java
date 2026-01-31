package com.moffd.app;

import com.moffd.app.Console.ConsoleUI;
import com.moffd.app.Utils.IOConsole;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConsoleUITest {
    private ConsoleUI consoleUI;

    @Before
    public void setup(){
        consoleUI = new ConsoleUI(new IOConsole());
    }

    @Test
    public void testValidateEmailCorrectDotCom(){
        String email = "email@domain.com";
        checkIfEmailIsValid(email);
    }

    @Test
    public void testValidateEmailCorrectDotNet(){
        String email = "email@domain.net";
        checkIfEmailIsValid(email);
    }

    @Test
    public void testValidateEmailCorrectDotOrg(){
        String email = "email@domain.org";
        checkIfEmailIsValid(email);
    }

    @Test
    public void testValidateEmailCorrectUnique(){
        String email = "email@domain.gg";
        checkIfEmailIsValid(email);
    }

    private void checkIfEmailIsValid(String email){
        String actual = consoleUI.validateEmail(email);

        assertNull(actual);
    }
}

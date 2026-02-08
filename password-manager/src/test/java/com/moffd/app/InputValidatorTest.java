package com.moffd.app;

import org.junit.Test;

import static com.moffd.app.Utils.InputValidator.validateEmail;
import static org.junit.Assert.*;

public class InputValidatorTest {

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
        String actual = validateEmail(email);

        assertNull(actual);
    }
}

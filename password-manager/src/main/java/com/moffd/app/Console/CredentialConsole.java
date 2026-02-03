package com.moffd.app.Console;

import com.moffd.app.Dao.CredentialDao;
import com.moffd.app.Models.Credential;
import com.moffd.app.Models.User;
import com.moffd.app.Utils.IOConsole;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CredentialConsole {
    private final User currentUser;
    private final IOConsole console;
    private final CredentialDao credentialDao;

    public CredentialConsole(User currentUser, IOConsole console) {
        this.currentUser = currentUser;
        this.console = console;
        this.credentialDao = new CredentialDao();
    }

    public void run() {
    }
}

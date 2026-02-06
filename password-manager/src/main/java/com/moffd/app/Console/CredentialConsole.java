package com.moffd.app.Console;

import com.moffd.app.Dao.CredentialDao;
import com.moffd.app.Models.UserSession;
import com.moffd.app.Utils.IOConsole;

import javax.crypto.spec.GCMParameterSpec;
import java.security.SecureRandom;

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
}

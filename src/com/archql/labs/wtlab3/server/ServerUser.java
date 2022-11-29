package com.archql.labs.wtlab3.server;

import com.archql.labs.wtlab3.logics.IUser;
import com.archql.labs.wtlab3.logics.Rights;

import java.util.EnumSet;
import java.util.Objects;

public class ServerUser {

    String login;
    String passwordHash;
    boolean sessionOpened;

    ServerUser() {
    }

    ServerUser(String login, String passwordHash) {
        this.login = login;
        this.passwordHash = passwordHash;
        sessionOpened = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServerUser that = (ServerUser) o;
        return sessionOpened == that.sessionOpened && login.equals(that.login) && passwordHash.equals(that.passwordHash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(login, passwordHash, sessionOpened);
    }
}

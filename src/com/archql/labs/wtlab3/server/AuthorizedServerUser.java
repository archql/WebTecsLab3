package com.archql.labs.wtlab3.server;

import com.archql.labs.wtlab3.logics.IUser;
import com.archql.labs.wtlab3.logics.Rights;

import java.util.Objects;

public class AuthorizedServerUser extends ServerUser implements IUser {

    int rights;

    AuthorizedServerUser() {
        super();
        this.rights = Rights.NONE.id;
    }
    AuthorizedServerUser(String login, String passwordHash, int rights) {
        super(login, passwordHash);
        this.rights = rights;
    }
    AuthorizedServerUser(ServerUser user, int rights) {
        super(user.login, user.passwordHash);
        this.rights = rights;
    }

    @Override
    public boolean hasRights(int rights) {
        return (this.rights & rights) == rights;
    }
    @Override
    public boolean hasRight(Rights right) {
        return (this.rights & right.id) != 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AuthorizedServerUser that = (AuthorizedServerUser) o;
        return rights == that.rights;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), rights);
    }
}

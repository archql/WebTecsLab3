package com.archql.labs.wtlab3.logics;

import java.util.EnumSet;

public interface IUser {
    boolean hasRights(int rights);
    boolean hasRight(Rights right);
}

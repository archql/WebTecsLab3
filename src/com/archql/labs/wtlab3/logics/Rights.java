package com.archql.labs.wtlab3.logics;


import java.util.HashMap;
import java.util.HashSet;

public enum Rights {
    NONE(0),
    GET(1),
    SET(2),
    NEW(4),
    DEL(8);

    public int id;

    Rights( int id ) {
        this.id = id;
    }

    Rights( String right ) {
        HashMap<String, Rights> rightsHashMap = new HashMap<>() {{
            put("NONE", NONE);
            put("GET", GET);
            put("SET", SET);
            put("NEW", NEW);
            put("DEL", DEL);
        }};
        this.id = rightsHashMap.get(right).id;
    }
}

package com.learnwords.webapp.models;

public enum Privacy {
    PRIVATE, PUBLIC;

    public Privacy getPrivacy(String value) {
        if (PUBLIC.name().equals(value))
            return PUBLIC;
        else if (PRIVATE.name().equals(value))
            return PRIVATE;
        else return null;
    }

}

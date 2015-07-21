package com.adzerk.android.sdk.rest;

/**
 * User information for requesting ads
 */
public class User  {

    private String key;

    public User(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}

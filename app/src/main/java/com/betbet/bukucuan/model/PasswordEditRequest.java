package com.betbet.bukucuan.model;

public class PasswordEditRequest {
    private final String password;
    private final Integer user_id;

    public PasswordEditRequest(Integer user_id, String password) {
        this.user_id = user_id;
        this.password = password;
    }
}
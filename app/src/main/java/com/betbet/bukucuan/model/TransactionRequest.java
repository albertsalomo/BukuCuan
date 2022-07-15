package com.betbet.bukucuan.model;

public class TransactionRequest {
    private final String id;
    private final Integer user_id;
    private final Integer category_id;
    private final String type;
    private final Integer amount;
    private final String description;
    private final String date;

    public TransactionRequest(String id, Integer user_id, Integer category_id, String type, Integer amount, String description
            , String date) {
        this.id = id;
        this.user_id = user_id;
        this.category_id = category_id;
        this.type = type;
        this.amount = amount;
        this.description = description;
        this.date = date;
    }
}

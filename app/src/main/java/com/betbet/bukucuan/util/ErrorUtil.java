package com.betbet.bukucuan.util;

import com.betbet.bukucuan.model.ErrorResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import retrofit2.Response;

public class ErrorUtil {
    public static String getMessage(Response response){
        // Converter JSON
        Gson gson = new GsonBuilder().create();
        ErrorResponse errorResponse = gson.fromJson(response.errorBody().charStream(),
                ErrorResponse.class);
        return errorResponse.getMessage();
    }
}

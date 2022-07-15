package com.betbet.bukucuan.retrofit;

import com.betbet.bukucuan.model.CategoryResponse;
import com.betbet.bukucuan.model.LoginResponse;
import com.betbet.bukucuan.model.PasswordEditRequest;
import com.betbet.bukucuan.model.SubmitResponse;
import com.betbet.bukucuan.model.TransactionRequest;
import com.betbet.bukucuan.model.TransactionResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface APIEndPoint {
    //List Transaction
    @GET("api/list_transaction.php")
    Call<TransactionResponse> listTransaction(
            @Query("user_id") Integer user_id
    );

    //Filter List Transaction
    @GET("api/list_transaction.php")
    Call<TransactionResponse> listTransactionFilter(
            @Query("user_id") Integer user_id,
            @Query("start_date") String start_date,
            @Query("end_date") String end_date
    );

    // Delete Transaction
    @DELETE("api/transaction_delete.php")
    Call<SubmitResponse> deleteTransaction(
            @Query("id") Integer id
    );

    // Update Transaction
    @PUT("api/transaction_edit.php")
    Call<SubmitResponse> editTransaction(
            @Body TransactionRequest transactionRequest
    );

    // Update Password
    @PUT("api/password_edit.php")
    Call<SubmitResponse> updatePassword(
            @Body PasswordEditRequest passwordEditRequest
    );

    // Login
    @FormUrlEncoded
    @POST("api/login.php")
    Call<LoginResponse> login(
            @Field("email") String email,
            @Field("password") String password
    );

    // Register
    @FormUrlEncoded
    @POST("api/register.php")
    Call<SubmitResponse> register(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password
    );

    //List Category
    @GET("api/list_category.php")
    Call<CategoryResponse> listCategory();

    // Create Transaction
    @FormUrlEncoded
    @POST("api/transaction.php")
    Call<SubmitResponse> transaction(
            @Field("user_id") Integer user_id,
            @Field("category_id") Integer category_id,
            @Field("type") String type,
            @Field("amount") Integer amount,
            @Field("description") String description,
            @Field("date") String date
    );
}

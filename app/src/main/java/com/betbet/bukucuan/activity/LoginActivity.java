package com.betbet.bukucuan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.betbet.bukucuan.R;
import com.betbet.bukucuan.databinding.ActivityLoginBinding;
import com.betbet.bukucuan.model.LoginResponse;
import com.betbet.bukucuan.preferences.PreferencesManager;
import com.betbet.bukucuan.retrofit.APIEndPoint;
import com.betbet.bukucuan.retrofit.APIService;
import com.betbet.bukucuan.util.ErrorUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {
    private final APIEndPoint api = APIService.endPoint();
    private ActivityLoginBinding binding;
    private PreferencesManager pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        pref = new PreferencesManager(this);
        setupListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        showProgress(false);
    }

    private void setupListener() {
        binding.textRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, WelcomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

        binding.buttonLogin.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if (isRequired()) {
                   showProgress(true);
                   api.login(
                           binding.editEmail.getText().toString(),
                           binding.editPassword.getText().toString()
                   ).enqueue(new Callback<LoginResponse>() {
                       //Response
                       @Override
                       public void onResponse(Call<LoginResponse> call,
                                              Response<LoginResponse> response) {
                           showProgress(false);
                           if (response.isSuccessful()) {
                               LoginResponse loginResponse = response.body();
                               Toast.makeText(getBaseContext(), "Login Success",
                                       Toast.LENGTH_SHORT).show();
                               loginResponse(loginResponse);
                               startActivity(new Intent(LoginActivity.this,
                                       HomeActivity.class));
                               finish();
                           } else {
                               Toast.makeText(LoginActivity.this,
                                       ErrorUtil.getMessage(response),
                                       Toast.LENGTH_SHORT).show();
                           }
                       }

                       @Override
                       public void onFailure(Call<LoginResponse> call, Throwable t) {
                           showProgress(false);
                           Toast.makeText(LoginActivity.this,
                                   "Failed to connect to the server !",
                                   Toast.LENGTH_SHORT).show();
                       }
                   });
               } else {
                   Toast.makeText(LoginActivity.this, "Fill the data correctly !",
                           Toast.LENGTH_SHORT).show();
               }
           }
       }
        );
    }

    private void showProgress(Boolean show) {
        if (show) {
            binding.progress.setVisibility(View.VISIBLE);
            binding.buttonLogin.setVisibility(View.GONE);
        } else {
            binding.progress.setVisibility(View.GONE);
            binding.buttonLogin.setVisibility(View.VISIBLE);
        }
    }

    private boolean isRequired() {
        return (!binding.editEmail.getText().toString().isEmpty() &&
                !binding.editPassword.getText().toString().isEmpty());
    }


    private void loginResponse(LoginResponse loginResponse) {
        LoginResponse.Data data = loginResponse.getData();
        pref.put("pref_is_login", true);
        pref.put("pref_user_id", data.getId());
        pref.put("pref_user_name", data.getName());
        pref.put("pref_user_email", data.getEmail());
        pref.put("pref_user_date", data.getDate());
        pref.put("pref_user_avatar", R.drawable.shiba);
        pref.put("pref_user_password", data.getPassword());
    }
}

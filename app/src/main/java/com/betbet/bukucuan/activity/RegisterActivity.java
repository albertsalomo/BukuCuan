package com.betbet.bukucuan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.betbet.bukucuan.activity.validation.EmailRule;
import com.betbet.bukucuan.activity.validation.PasswordRule;
import com.betbet.bukucuan.databinding.ActivityRegisterBinding;
import com.betbet.bukucuan.model.SubmitResponse;
import com.betbet.bukucuan.retrofit.APIEndPoint;
import com.betbet.bukucuan.retrofit.APIService;
import com.betbet.bukucuan.util.ErrorUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends BaseActivity {

    private final APIEndPoint api = APIService.endPoint();

    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        showProgress(false);
    }

    private void showProgress(Boolean show) {
        if (show) {
            binding.progress.setVisibility(View.VISIBLE);
            binding.buttonRegister.setVisibility(View.GONE);
        } else {
            binding.progress.setVisibility(View.GONE);
            binding.buttonRegister.setVisibility(View.VISIBLE);
        }
    }

    private void setupListener() {

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        binding.textLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

        binding.buttonRegister.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

              String email = binding.editEmail.getText().toString();
              String password = binding.editPassword.getText().toString();

              boolean isValid = validateEmail(email);
              isValid = validatePassword(password) && isValid;
              if (isValid) {
                  showProgress(true);
                  api.register(
                          binding.editName.getText().toString(),
                          binding.editEmail.getText().toString(),
                          binding.editPassword.getText().toString()
                  ).enqueue(new Callback<SubmitResponse>() {
                      @Override
                      public void onResponse(Call<SubmitResponse> call,
                                             Response<SubmitResponse> response) {
                          showProgress(false);
                          SubmitResponse submitResponse = response.body();
                          if (response.isSuccessful()) {
                              Intent intent = new Intent(getBaseContext(),
                                      LoginActivity.class);
                              startActivity(intent);
                              Toast.makeText(RegisterActivity.this,
                                      submitResponse.getMessage(), Toast.LENGTH_SHORT).show();
                              finish();
                          } else {
                              Toast.makeText(RegisterActivity.this,
                                      ErrorUtil.getMessage(response), Toast.LENGTH_SHORT).show();
                          }
                      }

                      @Override
                      public void onFailure(Call<SubmitResponse> call, Throwable t) {
                          Toast.makeText(RegisterActivity.this,
                                  "Please check your connection !",
                                  Toast.LENGTH_SHORT).show();
                          showProgress(false);
                      }
                  });
              }
          }
      }
        );

    }

    private boolean validatePassword(String password) {
        PasswordRule rule = new PasswordRule(password);
        rule.validate();

        if (!rule.isValid()) {
            binding.editPassword.setError(rule.getErrorMessage());
            return false;
        }

        String confirmPassword = binding.editConfirmPassword.getText().toString();
        if (!password.equals(confirmPassword)) {
            binding.editConfirmPassword.setError("Confirm Password does not match!");
            return false;
        } else {
            binding.editConfirmPassword.setError(null);
        }
        return true;
    }

    private boolean validateEmail(String email) {
        EmailRule rule = new EmailRule(email);
        rule.validate();
        binding.editEmail.setError(null);
        if (!rule.isValid()) {
            binding.editEmail.setError(rule.getErrorMessage());
            return false;
        }
        return true;
    }
}

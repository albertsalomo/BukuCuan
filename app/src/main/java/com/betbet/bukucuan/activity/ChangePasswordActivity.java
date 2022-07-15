package com.betbet.bukucuan.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.betbet.bukucuan.activity.validation.PasswordRule;
import com.betbet.bukucuan.databinding.ActivityChangePasswordBinding;
import com.betbet.bukucuan.model.PasswordEditRequest;
import com.betbet.bukucuan.model.SubmitResponse;
import com.betbet.bukucuan.preferences.PreferencesManager;
import com.betbet.bukucuan.retrofit.APIEndPoint;
import com.betbet.bukucuan.retrofit.APIService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends BaseActivity {

    private final APIEndPoint api = APIService.endPoint();
    private ActivityChangePasswordBinding binding;
    private PreferencesManager pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupListener();
        pref = new PreferencesManager(this);
    }

    private void setupListener() {
        binding.buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String oldPassword = binding.editOldPassword.getText().toString();
                String newPassword = binding.editNewPassword.getText().toString();

                boolean isValid = validateOldPassword(oldPassword);
                isValid = validatePassword(newPassword) && isValid;
                isValid = checkPasswordSecurity(oldPassword, newPassword) && isValid;

                PasswordEditRequest passwordEditRequest = new PasswordEditRequest(
                        pref.getInteger("pref_user_id"),
                        newPassword
                );

                if (isValid) {
                    api.updatePassword(passwordEditRequest).enqueue(new Callback<SubmitResponse>() {
                        @Override
                        public void onResponse(Call<SubmitResponse> call, Response<SubmitResponse>
                                response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(getApplicationContext(),
                                        "Password successfully updated !",
                                        Toast.LENGTH_SHORT).show();
                                pref.put("pref_user_password", newPassword);
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<SubmitResponse> call, Throwable t) {
                            Toast.makeText(getApplicationContext(),
                                    "Please check your internet connection !",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private boolean checkPasswordSecurity(String oldPassword, String newPassword) {
        if (oldPassword.equals(newPassword)) {
            binding.editNewPassword.setError("New password cannot be the same as old ones !");
            return false;
        }
        return true;
    }

    private boolean validateOldPassword(String password) {
        if (!pref.getString("pref_user_password").equals(password)) {
            binding.editOldPassword.setError("Old password does not match !");
            return false;
        }
        return true;
    }

    private boolean validatePassword(String password) {
        PasswordRule rule = new PasswordRule(password);
        rule.validate();

        if (!rule.isValid()) {
            binding.editNewPassword.setError(rule.getErrorMessage());
            return false;
        }
        String confirmPassword = binding.editNewConfirmPassword.getText().toString();
        if (!password.equals(confirmPassword)) {
            binding.editNewConfirmPassword.setError("Confirm Password does not match!");
            return false;
        } else {
            binding.editNewConfirmPassword.setError(null);
        }
        return true;
    }
}


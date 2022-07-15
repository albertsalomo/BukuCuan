package com.betbet.bukucuan.activity;

import android.os.Bundle;

import com.betbet.bukucuan.databinding.ActivityProfileBinding;

public class ProfileActivity extends BaseActivity {
    private ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
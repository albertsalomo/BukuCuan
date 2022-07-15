package com.betbet.bukucuan.activity;

import android.os.Bundle;
import android.view.View;

import com.betbet.bukucuan.databinding.ActivityTutorialBinding;

public class TutorialActivity extends BaseActivity {

    private ActivityTutorialBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTutorialBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupListener();
    }

    private void navigationBinding() {
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setupListener() {
        navigationBinding();
    }
}


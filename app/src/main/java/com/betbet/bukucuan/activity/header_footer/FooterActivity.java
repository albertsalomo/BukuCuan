package com.betbet.bukucuan.activity.header_footer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.betbet.bukucuan.R;
import com.betbet.bukucuan.activity.BaseActivity;
import com.betbet.bukucuan.activity.HomeActivity;
import com.betbet.bukucuan.activity.TransactionActivity;
import com.betbet.bukucuan.databinding.ActivityFooterBinding;
import com.betbet.bukucuan.databinding.ActivityHomeBinding;

public class FooterActivity extends BaseActivity {

    private ActivityFooterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFooterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}
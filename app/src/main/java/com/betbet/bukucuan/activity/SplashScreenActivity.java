package com.betbet.bukucuan.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.betbet.bukucuan.R;
import com.betbet.bukucuan.databinding.ActivitySplashScreenBinding;
import com.betbet.bukucuan.preferences.PreferencesManager;

public class SplashScreenActivity extends AppCompatActivity {
    private static final int DELAYED = 5000;
    ActivitySplashScreenBinding binding;
    Animation topAnim, sideAnim, bottomAnim, devAnim;
    private PreferencesManager pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getSupportActionBar().hide();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(binding.getRoot());
        pref = new PreferencesManager(this);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_anim);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_anim);
        sideAnim = AnimationUtils.loadAnimation(this, R.anim.side_anim);
        devAnim = AnimationUtils.loadAnimation(this, R.anim.dev_splash_bottom_anim);

        // Set Animation
        binding.logoDev.setAnimation(devAnim);
        binding.labelDev.setAnimation(devAnim);
        binding.labelSubtitle.setAnimation(bottomAnim);
        binding.splashLogo.setAnimation(topAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (pref.getBoolean("pref_is_login")) {
                    Intent intent = new Intent(getBaseContext(),
                            HomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = new Intent(getBaseContext(), WelcomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, DELAYED);
    }
}

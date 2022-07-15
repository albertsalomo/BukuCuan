package com.betbet.bukucuan.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.betbet.bukucuan.R;
import com.betbet.bukucuan.databinding.ActivityGraphBinding;
import com.betbet.bukucuan.preferences.PreferencesManager;

public class GraphActivity extends ExitAppActivity {
    private ActivityGraphBinding binding;
    private PreferencesManager pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGraphBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        pref = new PreferencesManager(this);
        setupListener();
        setAvatar();
    }

    private void setupListener() {

        binding.transactionHeader.tutorialNavIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GraphActivity.this, TutorialActivity.class);
                startActivity(intent);
            }
        });


        binding.transactionHeader.imageAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GraphActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        // Graph Setup Icon
        binding.transactionFooter.iconNavGraph.setColorFilter(ContextCompat.getColor(
                GraphActivity.this, R.color.white),
                android.graphics.PorterDuff.Mode.SRC_IN);

        binding.transactionFooter.labelNavGraph.setTextColor(Color.
                parseColor("#FFFFFFFF"));

        binding.transactionFooter.buttonNavTransaction.
                setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getBaseContext(), TransactionActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                    }
                });

        binding.transactionFooter.buttonNavHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });


    }

    private void setAvatar() {
        binding.transactionHeader.textAvatar.setText(pref.getString("pref_user_name"));
        binding.transactionHeader.imageAvatar.setImageResource(pref.
                getInteger("pref_user_avatar"));
    }
}


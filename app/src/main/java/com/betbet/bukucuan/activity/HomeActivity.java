package com.betbet.bukucuan.activity;

import static java.lang.Math.abs;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.betbet.bukucuan.R;
import com.betbet.bukucuan.databinding.ActivityHomeBinding;
import com.betbet.bukucuan.model.TransactionResponse;
import com.betbet.bukucuan.preferences.PreferencesManager;
import com.betbet.bukucuan.retrofit.APIEndPoint;
import com.betbet.bukucuan.retrofit.APIService;
import com.betbet.bukucuan.util.FormatUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends ExitAppActivity {
    private final APIEndPoint api = APIService.endPoint();
    private ActivityHomeBinding binding;
    private PreferencesManager pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        pref = new PreferencesManager(this);
        setupListener();
        getTransaction();
        setAvatar();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setAvatar();
        getTransaction();
    }

    private void navigationBar() {

        binding.transactionHeader.tutorialNavIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, TutorialActivity.class);
                startActivity(intent);
            }
        });

        binding.transactionFooter.
                iconNavHome.setColorFilter(ContextCompat.
                        getColor(HomeActivity.this, R.color.white),
                android.graphics.PorterDuff.Mode.SRC_IN);

        binding.transactionFooter.labelNavHome.setTextColor(Color.parseColor("#FFFFFFFF"));

        binding.transactionFooter.buttonNavTransaction.setOnClickListener
                (new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getBaseContext(), TransactionActivity.class);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                        finish();
                    }
                });

        binding.transactionFooter.buttonNavGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), GraphActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
    }

    private void avatarBinding() {
        binding.transactionHeader.imageAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class)
                        .putExtra("balance", binding.textBalance.getText().toString());
                startActivity(intent);
            }
        });
    }

    private void contentBinding() {
        binding.transactionDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,
                        TransactionActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        binding.developerProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,
                        DeveloperProfileActivity.class);
                startActivity(intent);
            }
        });

        binding.addTransactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,
                        CreateActivity.class);
                startActivity(intent);
            }
        });

        binding.graphAnalyticsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this,
                        GraphActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });
    }

    private void setupListener() {
        navigationBar();
        avatarBinding();
        contentBinding();
    }

    // Show the transaction list to recycler view
    private void getTransaction() {
        api.listTransaction(pref.getInteger("pref_user_id")).
                enqueue(new Callback<TransactionResponse>() {
                    @Override
                    public void onResponse(Call<TransactionResponse> call,
                                           Response<TransactionResponse> response) {
                        if (response.isSuccessful()) {
                            TransactionResponse transactionResponse = response.body();
                            setBalance(transactionResponse);
                        }
                    }

                    @Override
                    public void onFailure(Call<TransactionResponse> call, Throwable t) {

                    }
                });
    }

    private void setBalance(TransactionResponse transactionResponse) {
        if (transactionResponse.getBalance() < 0) {
            binding.textBalance.
                    setText("Balance : - Rp " + FormatUtil.number(abs(transactionResponse.
                            getBalance())));
            binding.textBalance.setTextColor(Color.parseColor("#FF5C5C"));
        } else {
            binding.textBalance.setText("Balance : Rp " + FormatUtil.number(transactionResponse.
                    getBalance()));
            binding.textBalance.setTextColor(Color.parseColor("#68CA45"));
        }
        binding.textIn.setText("Rp " + FormatUtil.number(transactionResponse.getTotal_in()));
        binding.textOut.setText("Rp " + FormatUtil.number(transactionResponse.getTotal_out()));
    }

    private void setAvatar() {
        binding.transactionHeader.textAvatar.setText(pref.
                getString("pref_user_name"));
        binding.transactionHeader.imageAvatar.setImageResource(pref.
                getInteger("pref_user_avatar"));
    }
}


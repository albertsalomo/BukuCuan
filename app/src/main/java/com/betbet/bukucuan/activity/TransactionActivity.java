package com.betbet.bukucuan.activity;

import static java.lang.Math.abs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.betbet.bukucuan.R;
import com.betbet.bukucuan.adapter.TransactionAdapter;
import com.betbet.bukucuan.databinding.ActivityTransactionBinding;
import com.betbet.bukucuan.model.SubmitResponse;
import com.betbet.bukucuan.model.TransactionResponse;
import com.betbet.bukucuan.preferences.PreferencesManager;
import com.betbet.bukucuan.retrofit.APIEndPoint;
import com.betbet.bukucuan.retrofit.APIService;
import com.betbet.bukucuan.util.FormatUtil;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionActivity extends ExitAppActivity {
    private final APIEndPoint api = APIService.endPoint();
    private final Integer filterRequestCode = 123;
    private ActivityTransactionBinding binding;
    private TransactionAdapter transactionAdapter;
    private PreferencesManager pref;
    private Boolean filterTransaction = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTransactionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        pref = new PreferencesManager(this);
        setupRecyclerView();
        setupListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setAvatar();
        if (!filterTransaction) {
            getTransaction();
        }
    }

    private void setupRecyclerView() {
        transactionAdapter = new TransactionAdapter(new ArrayList<>(),
                new TransactionAdapter.AdapterListener() {
                    // Update
                    @Override
                    public void onClick(TransactionResponse.Data data) {
                        startActivity(new Intent(TransactionActivity.this,
                                UpdateActivity.class)
                                .putExtra("intent_data", data));
                    }

                    // Delete
                    @Override
                    public void onLongClick(TransactionResponse.Data data) {
                        AlertDialog alertDialog = new AlertDialog.
                                Builder(TransactionActivity.this).create();
                        alertDialog.setTitle("Delete Data");
                        alertDialog.setMessage("Delete " + data.getDescription() + " transaction?");
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Delete",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        deleteTransaction(data.getId());
                                    }
                                });
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel  ",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                });
        binding.listTransaction.setAdapter(transactionAdapter);
    }

    private String filterDate(int day, int month, int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);

        //Filter
        calendar.add(Calendar.DAY_OF_MONTH, day);
        calendar.add(Calendar.MONTH, month);
        calendar.add(Calendar.YEAR, year);

        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

        String filterDate = mYear + "-" + mMonth + "-" + mDay;

        return filterDate;
    }

    private void navigationBar() {

        binding.transactionHeader.tutorialNavIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TransactionActivity.this,
                        TutorialActivity.class);
                startActivity(intent);
            }
        });

        binding.analyticalGraphButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TransactionActivity.this,
                        GraphActivity.class);
                startActivity(intent);
                finish();
            }
        });

        binding.transactionFooter.iconNavTransaction.setColorFilter(ContextCompat.
                        getColor(TransactionActivity.this, R.color.white),
                android.graphics.PorterDuff.Mode.SRC_IN);

        binding.transactionFooter.labelNavTransaction.
                setTextColor(Color.parseColor("#FFFFFFFF"));

        binding.transactionFooter.buttonNavHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), HomeActivity.class);
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
                Intent intent = new Intent(TransactionActivity.this, ProfileActivity.class)
                        .putExtra("balance", binding.textBalance.getText().toString());
                startActivity(intent);
            }
        });
    }

    private void setFilterButtonDefault() {
        binding.allTimeFilter.setBackgroundTintList(ColorStateList.valueOf(getResources().
                getColor(R.color.gray)));
        binding.weekDateFilter.setBackgroundTintList(ColorStateList.valueOf(getResources().
                getColor(R.color.gray)));
        binding.monthDateFilter.setBackgroundTintList(ColorStateList.valueOf(getResources().
                getColor(R.color.gray)));
        binding.yearDateFilter.setBackgroundTintList(ColorStateList.valueOf(getResources().
                getColor(R.color.gray)));
        binding.dateFilter.setBackgroundTintList(ColorStateList.valueOf(getResources().
                getColor(R.color.gray)));
        binding.todayDateFilter.setBackgroundTintList(ColorStateList.valueOf(getResources().
                getColor(R.color.gray)));
    }

    private void transactionFilterBinding() {
        binding.allTimeFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTransaction();
                setFilterButtonDefault();
                binding.allTimeFilter.setBackgroundTintList(ColorStateList.
                        valueOf(getResources().getColor(R.color.yellow)));
            }
        });
        binding.todayDateFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String startDate = filterDate(-1, 0, 0);
                String endDate = filterDate(0, 0, 0);
                api.listTransactionFilter(
                        pref.getInteger("pref_user_id"),
                        startDate,
                        endDate
                ).enqueue(new Callback<TransactionResponse>() {
                    @Override
                    public void onResponse(Call<TransactionResponse> call,
                                           Response<TransactionResponse> response) {
                        filterTransaction = false;
                        if (response.isSuccessful()) {
                            TransactionResponse transactionResponse = response.body();
                            setBalance(transactionResponse);
                            transactionAdapter.setData(transactionResponse.getData());
                            setFilterButtonDefault();
                            binding.todayDateFilter.setBackgroundTintList(ColorStateList.
                                    valueOf(getResources().getColor(R.color.yellow)));
                        }
                    }

                    @Override
                    public void onFailure(Call<TransactionResponse> call, Throwable t) {
                        Log.e("TransactionActivity", t.toString());
                        filterTransaction = false;
                    }
                });
            }
        });
        binding.weekDateFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String startDate = filterDate(-7, 0, 0);
                String endDate = filterDate(0, 0, 0);
                api.listTransactionFilter(
                        pref.getInteger("pref_user_id"),
                        startDate,
                        endDate
                ).enqueue(new Callback<TransactionResponse>() {
                    @Override
                    public void onResponse(Call<TransactionResponse> call,
                                           Response<TransactionResponse> response) {
                        filterTransaction = false;
                        if (response.isSuccessful()) {
                            TransactionResponse transactionResponse = response.body();
                            setBalance(transactionResponse);
                            transactionAdapter.setData(transactionResponse.getData());
                            setFilterButtonDefault();
                            binding.weekDateFilter.setBackgroundTintList(ColorStateList.
                                    valueOf(getResources().getColor(R.color.yellow)));
                        }
                    }

                    @Override
                    public void onFailure(Call<TransactionResponse> call, Throwable t) {
                        Log.e("TransactionActivity", t.toString());
                        filterTransaction = false;
                    }
                });
            }
        });
        binding.monthDateFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String startDate = filterDate(0, -1, 0);
                String endDate = filterDate(0, 0, 0);
                api.listTransactionFilter(
                        pref.getInteger("pref_user_id"),
                        startDate,
                        endDate
                ).enqueue(new Callback<TransactionResponse>() {
                    @Override
                    public void onResponse(Call<TransactionResponse> call,
                                           Response<TransactionResponse> response) {
                        filterTransaction = false;
                        if (response.isSuccessful()) {
                            TransactionResponse transactionResponse = response.body();
                            setBalance(transactionResponse);
                            transactionAdapter.setData(transactionResponse.getData());
                            setFilterButtonDefault();
                            binding.monthDateFilter.setBackgroundTintList(ColorStateList.
                                    valueOf(getResources().getColor(R.color.yellow)));
                        }
                    }

                    @Override
                    public void onFailure(Call<TransactionResponse> call, Throwable t) {
                        Log.e("TransactionActivity", t.toString());
                        filterTransaction = false;
                    }
                });
            }
        });
        binding.yearDateFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String startDate = filterDate(0, 0, -1);
                String endDate = filterDate(0, 0, 0);
                api.listTransactionFilter(
                        pref.getInteger("pref_user_id"),
                        startDate,
                        endDate
                ).enqueue(new Callback<TransactionResponse>() {
                    @Override
                    public void onResponse(Call<TransactionResponse> call,
                                           Response<TransactionResponse> response) {
                        filterTransaction = false;
                        if (response.isSuccessful()) {
                            TransactionResponse transactionResponse = response.body();
                            setBalance(transactionResponse);
                            transactionAdapter.setData(transactionResponse.getData());
                            setFilterButtonDefault();
                            binding.yearDateFilter.setBackgroundTintList(ColorStateList.
                                    valueOf(getResources().getColor(R.color.yellow)));
                        }
                    }

                    @Override
                    public void onFailure(Call<TransactionResponse> call, Throwable t) {
                        Log.e("TransactionActivity", t.toString());
                        filterTransaction = false;
                    }
                });
            }
        });
        binding.dateFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterTransaction = true;
                Intent intent = new Intent(getBaseContext(), FilterActivity.class);
                startActivityForResult(intent, filterRequestCode);
            }
        });
    }

    private void setupListener() {
        navigationBar();
        avatarBinding();
        transactionFilterBinding();
        binding.fabCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), CreateActivity.class);
                startActivity(intent);
            }
        });
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
                            transactionAdapter.setData(transactionResponse.getData());
                            setFilterButtonDefault();
                            binding.allTimeFilter.setBackgroundTintList(ColorStateList.
                                    valueOf(getResources().getColor(R.color.yellow)));
                        }
                    }

                    @Override
                    public void onFailure(Call<TransactionResponse> call, Throwable t) {

                    }
                });
    }

    private void setBalance(TransactionResponse transactionResponse) {
        if (transactionResponse.getBalance() < 0) {
            binding.textBalance.setText("- Rp " + FormatUtil.number(abs(transactionResponse.
                    getBalance())));
            binding.textBalance.setTextColor(Color.parseColor("#FF5C5C"));
        } else {
            binding.textBalance.setText("Rp " + FormatUtil.number(transactionResponse.getBalance()));
            binding.textBalance.setTextColor(Color.parseColor("#68CA45"));
        }
        binding.textIn.setText("Rp " + FormatUtil.number(transactionResponse.getTotal_in()));
        binding.textOut.setText("Rp " + FormatUtil.number(transactionResponse.getTotal_out()));
    }


    private void setAvatar() {
        binding.transactionHeader.textAvatar.setText(pref.getString("pref_user_name"));
        binding.transactionHeader.imageAvatar.setImageResource(pref.getInteger("pref_user_avatar"));
    }

    private void deleteTransaction(Integer id) {
        api.deleteTransaction(id).enqueue(new Callback<SubmitResponse>() {
            @Override
            public void onResponse(Call<SubmitResponse> call, Response<SubmitResponse> response) {
                if (response.isSuccessful()) {
                    getTransaction();
                }
            }

            @Override
            public void onFailure(Call<SubmitResponse> call, Throwable t) {

            }
        });
    }

    // After Date Filter
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == filterRequestCode && resultCode == Activity.RESULT_OK) {
            String dateStart = data.getStringExtra("date_start");
            String dateEnd = data.getStringExtra("date_end");

            api.listTransactionFilter(
                    pref.getInteger("pref_user_id"),
                    dateStart,
                    dateEnd
            ).enqueue(new Callback<TransactionResponse>() {
                @Override
                public void onResponse(Call<TransactionResponse> call,
                                       Response<TransactionResponse> response) {
                    filterTransaction = false;
                    if (response.isSuccessful()) {
                        TransactionResponse transactionResponse = response.body();
                        setBalance(transactionResponse);
                        transactionAdapter.setData(transactionResponse.getData());
                        setFilterButtonDefault();
                        binding.dateFilter.setBackgroundTintList(ColorStateList.valueOf(getResources().
                                getColor(R.color.yellow)));
                    }
                }

                @Override
                public void onFailure(Call<TransactionResponse> call, Throwable t) {
                    Log.e("TransactionActivity", t.toString());
                    filterTransaction = false;
                }
            });
        }
    }
}

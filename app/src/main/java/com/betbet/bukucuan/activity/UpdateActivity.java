package com.betbet.bukucuan.activity;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.core.view.ViewCompat;

import com.betbet.bukucuan.R;
import com.betbet.bukucuan.adapter.CategoryAdapter;
import com.betbet.bukucuan.databinding.ActivityCreateBinding;
import com.betbet.bukucuan.model.CategoryResponse;
import com.betbet.bukucuan.model.SubmitResponse;
import com.betbet.bukucuan.model.TransactionRequest;
import com.betbet.bukucuan.model.TransactionResponse;
import com.betbet.bukucuan.preferences.PreferencesManager;
import com.betbet.bukucuan.retrofit.APIEndPoint;
import com.betbet.bukucuan.retrofit.APIService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateActivity extends BaseActivity {

    private final String TAG = "UpdateActivity";
    private final APIEndPoint api = APIService.endPoint();
    private ActivityCreateBinding binding;
    private TransactionResponse.Data data;
    private PreferencesManager pref;
    private CategoryAdapter categoryAdapter;
    private String type = "";
    private Integer categoryId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        pref = new PreferencesManager(this);
        setupRecyclerView();
        setupListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getTransaction();
        getCategory();
    }

    private void setupRecyclerView() {
        categoryAdapter = new CategoryAdapter(UpdateActivity.this, new ArrayList<>(),
                new CategoryAdapter.AdapterListener() {
                    @Override
                    public void onClick(CategoryResponse.Data data) {
                        categoryId = data.getId();
                        Log.e(TAG, "categoryId" + categoryId);
                    }
                });
        binding.listCategory.setAdapter(categoryAdapter);
    }

    private void setupListener() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        binding.dateTransaction.setMaxDate(calendar.getTimeInMillis());

        binding.buttonIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = "IN";
                binding.buttonIn.setTextColor(getResources().getColor(R.color.white));
                // Button In
                ViewCompat.setBackgroundTintList(
                        binding.buttonIn, ColorStateList.valueOf(getResources().
                                getColor(R.color.in_color))
                );

                // Button Out
                binding.buttonOut.setTextColor(getResources().getColor(R.color.black));
                ViewCompat.setBackgroundTintList(
                        binding.buttonOut, ColorStateList.valueOf(getResources().
                                getColor(R.color.white))
                );
            }
        });
        binding.buttonOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = "OUT";
                binding.buttonOut.setTextColor(getResources().getColor(R.color.white));
                // Button In
                ViewCompat.setBackgroundTintList(
                        binding.buttonOut, ColorStateList.valueOf(getResources().
                                getColor(R.color.out_color))
                );

                // Button Out
                binding.buttonIn.setTextColor(getResources().getColor(R.color.black));
                ViewCompat.setBackgroundTintList(
                        binding.buttonIn, ColorStateList.valueOf(getResources().
                                getColor(R.color.white))
                );
            }
        });
        binding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = binding.dateTransaction.getYear();
                int month = binding.dateTransaction.getMonth() + 1;
                int day = binding.dateTransaction.getDayOfMonth();

                String insertDate = year + "-" + month + "-" + day;
                TransactionRequest transactionRequest = new TransactionRequest(
                        data.getId().toString(),
                        pref.getInteger("pref_user_id"),
                        categoryId,
                        type,
                        Integer.parseInt(binding.editAmount.getText().toString()),
                        binding.editNote.getText().toString(),
                        insertDate
                );

                api.editTransaction(transactionRequest).enqueue(new Callback<SubmitResponse>() {
                    @Override
                    public void onResponse(Call<SubmitResponse> call,
                                           Response<SubmitResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),
                                    "Transaction successfully updated !",
                                    Toast.LENGTH_SHORT).show();
                        }
                        finish();
                    }

                    @Override
                    public void onFailure(Call<SubmitResponse> call, Throwable t) {

                    }
                });
            }
        });
    }

    private void getTransaction() {
        data = (TransactionResponse.Data) getIntent().getSerializableExtra("intent_data");
        // Date
        String date = data.getDate();
        String[] values = date.split("-");
        int year = Integer.parseInt(values[0]);
        int month = Integer.parseInt(values[1]);
        int day = Integer.parseInt(values[2]);
        binding.dateTransaction.updateDate(year, month - 1, day);

        Log.e(TAG, "intentData" + data.toString());
        type = data.getType();
        if (type.equals("IN")) {
            binding.buttonIn.setTextColor(getResources().getColor(R.color.white));
            // Button In
            ViewCompat.setBackgroundTintList(
                    binding.buttonIn, ColorStateList.valueOf(getResources().
                            getColor(R.color.in_color))
            );
        } else {
            binding.buttonOut.setTextColor(getResources().getColor(R.color.white));
            // Button In
            ViewCompat.setBackgroundTintList(
                    binding.buttonOut, ColorStateList.valueOf(getResources().
                            getColor(R.color.out_color))
            );
        }
        binding.editAmount.setText(data.getAmount().toString());
        binding.editNote.setText(data.getDescription());
    }

    private void getCategory() {
        api.listCategory().enqueue(
                new Callback<CategoryResponse>() {
                    @Override
                    public void onResponse(Call<CategoryResponse> call,
                                           Response<CategoryResponse> response) {
                        if (response.isSuccessful()) {
                            List<CategoryResponse.Data> dataList = response.body().getData();
                            categoryAdapter.setData(dataList);
                            for (CategoryResponse.Data category : dataList) {
                                if (category.getName().contains(data.getCategory())) {
                                    Log.e(TAG, category.getName());
                                    categoryAdapter.setButtonList(category);
                                    categoryId = category.getId();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<CategoryResponse> call, Throwable t) {
                    }
                }
        );
    }
}


package com.betbet.bukucuan.activity;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import androidx.core.view.ViewCompat;

import com.betbet.bukucuan.R;
import com.betbet.bukucuan.adapter.CategoryAdapter;
import com.betbet.bukucuan.databinding.ActivityCreateBinding;
import com.betbet.bukucuan.model.CategoryResponse;
import com.betbet.bukucuan.model.SubmitResponse;
import com.betbet.bukucuan.preferences.PreferencesManager;
import com.betbet.bukucuan.retrofit.APIEndPoint;
import com.betbet.bukucuan.retrofit.APIService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateActivity extends BaseActivity {

    private final String TAG = "Create Activity";
    private final APIEndPoint api = APIService.endPoint();
    private ActivityCreateBinding binding;
    private TransactionActivity transactionActivity;
    private PreferencesManager pref;
    private CategoryAdapter categoryAdapter;
    private String type = "";
    private Integer category = 0;

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
        getCategory();
    }

    private void setupRecyclerView() {
        categoryAdapter = new CategoryAdapter(CreateActivity.this, new ArrayList<>(),
                new CategoryAdapter.AdapterListener() {
                    @Override
                    public void onClick(CategoryResponse.Data data) {
                        category = data.getId();
                    }
                });
        binding.listCategory.setAdapter(categoryAdapter);
    }


    private void amountFormat(){
        if(binding.editAmount.getText().length() % 3 == 0){
            binding.editAmount.setText(binding.editAmount.getText() + ",");
        }
    }

    private void setupListener() {
        Calendar calendar = Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        binding.dateTransaction.setMaxDate(calendar.getTimeInMillis());
        binding.dateTransaction.updateDate(mYear, mMonth, mDay);

        binding.editAmount.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                return false;
            }
        });

        // Saat Button In ditekan
        binding.buttonIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                type = "IN";
                binding.buttonIn.setTextColor(getResources().getColor(R.color.white));
                // Button In
                ViewCompat.setBackgroundTintList(
                        binding.buttonIn,
                        ColorStateList.valueOf(getResources().getColor(R.color.in_color))
                );

                // Button Out
                binding.buttonOut.setTextColor(getResources().getColor(R.color.black));
                ViewCompat.setBackgroundTintList(
                        binding.buttonOut,
                        ColorStateList.valueOf(getResources().getColor(R.color.white))
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
                        binding.buttonOut,
                        ColorStateList.valueOf(getResources().getColor(R.color.out_color))
                );

                // Button Out
                binding.buttonIn.setTextColor(getResources().getColor(R.color.black));
                ViewCompat.setBackgroundTintList(
                        binding.buttonIn,
                        ColorStateList.valueOf(getResources().getColor(R.color.white))
                );
            }
        });
        // Save Transaction
        binding.buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = binding.dateTransaction.getYear();
                int month = binding.dateTransaction.getMonth() + 1;
                int day = binding.dateTransaction.getDayOfMonth();

                String insertDate = year + "-" + month + "-" + day;

                if (isRequired()) {
                    binding.buttonSave.setEnabled(false);
                    api.transaction(
                            pref.getInteger("pref_user_id"),
                            category,
                            type,
                            Integer.parseUnsignedInt(binding.editAmount.getText().toString()),
                            binding.editNote.getText().toString(),
                            insertDate
                    ).enqueue(new Callback<SubmitResponse>() {
                        @Override
                        public void onResponse(Call<SubmitResponse> call,
                                               Response<SubmitResponse> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(getApplicationContext(),
                                        "Transaction successfully inserted !",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<SubmitResponse> call, Throwable t) {

                        }
                    });
                }
            }
        });

    }

    private void getCategory() {
        api.listCategory().enqueue(
                new Callback<CategoryResponse>() {
                    @Override
                    public void onResponse(Call<CategoryResponse> call,
                                           Response<CategoryResponse> response) {
                        if (response.isSuccessful()) {
                            List<CategoryResponse.Data> dataList = response.body().getData();
                            Log.e(TAG, dataList.toString());
                            categoryAdapter.setData(dataList);
                        }
                    }

                    @Override
                    public void onFailure(Call<CategoryResponse> call, Throwable t) {
                    }
                }
        );
    }

    private Boolean isRequired() {
        if (type.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Choose transaction type !",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (category == 0) {
            Toast.makeText(getApplicationContext(), "Choose category !",
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (binding.editAmount.getText().toString().isEmpty()) {
            binding.editAmount.setError("Insert transaction amount !");
            return false;
        } else if (binding.editNote.getText().toString().isEmpty()) {
            binding.editNote.setError("Insert note transaction !");
            return false;
        }
        return true;
    }
}



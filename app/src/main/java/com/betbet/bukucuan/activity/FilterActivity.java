package com.betbet.bukucuan.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;

import com.betbet.bukucuan.databinding.ActivityFilterBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FilterActivity extends BaseActivity {
    final Calendar calendar = Calendar.getInstance();
    final Calendar calendar2 = Calendar.getInstance();
    private ActivityFilterBinding binding;
    private Integer dateType = 0;
    private String dateStart = "";
    private String dateEnd = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFilterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupListener();
    }

    private void setupListener() {
        binding.editStart.setOnClickListener(new View.OnClickListener() {
            DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, day);
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);
                    setDate();
                }
            };
            @Override
            public void onClick(View view) {
                DatePickerDialog d = new DatePickerDialog(
                        FilterActivity.this,
                        dateListener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );
                d.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                d.show();
                dateType = 1;
            }
        });

        binding.editEnd.setOnClickListener(new View.OnClickListener() {
            DatePickerDialog.OnDateSetListener dateListener2 = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    calendar2.set(Calendar.YEAR, year);
                    calendar2.set(Calendar.MONTH, month);
                    calendar2.set(Calendar.DAY_OF_MONTH, day);
                    calendar2.set(Calendar.HOUR_OF_DAY, 0);
                    calendar2.set(Calendar.MINUTE, 0);
                    calendar2.set(Calendar.SECOND, 0);
                    calendar2.set(Calendar.MILLISECOND, 0);
                    setDate();
                }
            };
            @Override
            public void onClick(View view) {
                DatePickerDialog e = new DatePickerDialog(
                        FilterActivity.this,
                        dateListener2,
                        calendar2.get(Calendar.YEAR),
                        calendar2.get(Calendar.MONTH),
                        calendar2.get(Calendar.DAY_OF_MONTH)
                );
                e.getDatePicker().setMaxDate(calendar2.getTimeInMillis());
                e.show();
                dateType = 2;
            }
        });
        binding.buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!dateStart.isEmpty() && !dateEnd.isEmpty()) {
                    Intent intent = new Intent();
                    intent.putExtra("date_start", dateStart);
                    intent.putExtra("date_end", dateEnd);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    private void setDate() {
        if (dateType.equals(1)) {
            dateStart = dateFormat(calendar.getTime(), "yyyy-MM-dd");
            binding.editStart.setText(dateFormat(
                    calendar.getTime(), "MMM, dd yyyy")
            );
        } else if (dateType.equals(2)) {
            dateEnd = dateFormat(calendar2.getTime(), "yyyy-MM-dd");
            binding.editEnd.setText(dateFormat(
                    calendar2.getTime(), "MMM-dd-yyyy")
            );
        }
    }

    private String dateFormat(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return dateFormat.format(date);
    }
}
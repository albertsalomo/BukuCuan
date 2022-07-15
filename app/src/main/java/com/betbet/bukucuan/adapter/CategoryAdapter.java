package com.betbet.bukucuan.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.betbet.bukucuan.R;
import com.betbet.bukucuan.databinding.AdapterCategoryBinding;
import com.betbet.bukucuan.model.CategoryResponse;
import com.google.android.material.button.MaterialButton;
import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private final List<CategoryResponse.Data> dataList;
    private final AdapterListener listener;
    private final Context context;
    private final List<MaterialButton> buttonList = new ArrayList<>();

    public CategoryAdapter(Context context, List<CategoryResponse.Data> dataList,
                           AdapterListener listener) {
        this.context = context;
        this.dataList = dataList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                AdapterCategoryBinding
                        .inflate(LayoutInflater.from(parent.getContext()), parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder,
                                 @SuppressLint("RecyclerView") int position) {
        final CategoryResponse.Data data = dataList.get(position);
        holder.binding.buttonCategory.setText(data.getName());
        buttonList.add(holder.binding.buttonCategory);
        holder.binding.buttonCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(dataList.get(position));
                setButtonList(holder.binding.buttonCategory);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void setData(List<CategoryResponse.Data> data) {
        dataList.clear();
        dataList.addAll(data);
        notifyDataSetChanged();
    }

    private void setButtonList(MaterialButton materialButton) {
        // Balikin warna button yang lain jadi default
        for (MaterialButton button : buttonList) {
            button.setTextColor(context.getResources().getColor(R.color.black));
            // Button In
            ViewCompat.setBackgroundTintList(
                    button, ColorStateList.valueOf(context.getResources().
                            getColor(R.color.white))
            );
        }
        materialButton.setTextColor(context.getResources().getColor(R.color.white));
        ViewCompat.setBackgroundTintList(
                materialButton, ColorStateList.valueOf(context.getResources().
                        getColor(R.color.yellow))
        );
    }

    public void setButtonList(CategoryResponse.Data category) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (MaterialButton button : buttonList) {
                    if (button.getText().toString().contains(category.getName())) {
                        button.setTextColor(context.getResources().getColor(R.color.white));
                        ViewCompat.setBackgroundTintList(
                                button, ColorStateList.valueOf(context.getResources().
                                        getColor(R.color.yellow))
                        );
                    }
                }
            }
        }, 50);
    }

    public interface AdapterListener {
        void onClick(CategoryResponse.Data data);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AdapterCategoryBinding binding;

        public ViewHolder(AdapterCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

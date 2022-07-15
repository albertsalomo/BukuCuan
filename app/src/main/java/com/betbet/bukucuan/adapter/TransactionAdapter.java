package com.betbet.bukucuan.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.betbet.bukucuan.R;
import com.betbet.bukucuan.databinding.AdapterTransactionBinding;
import com.betbet.bukucuan.model.TransactionResponse;
import com.betbet.bukucuan.util.FormatUtil;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private final List<TransactionResponse.Data> dataList;
    private final AdapterListener listener;

    public TransactionAdapter(List<TransactionResponse.Data> dataList, AdapterListener listener) {
        this.dataList = dataList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TransactionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new ViewHolder(
                AdapterTransactionBinding
                        .inflate(LayoutInflater.from(parent.getContext()), parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionAdapter.ViewHolder holder, int position) {

        // Data format in list
        final TransactionResponse.Data data = dataList.get(position);
        holder.binding.textCategory.setText(data.getCategory());
        holder.binding.textDescription.setText(data.getDescription());
        holder.binding.textDate.setText(FormatUtil.dateAdapter(data.getDate()));
        holder.binding.textAmount.setText("Rp " + FormatUtil.number(data.getAmount()));

        if (data.getType().equals("IN")) {
            holder.binding.imageType.setImageResource(R.drawable.ic_up_transaction);
            holder.binding.imageType.setBackgroundResource(R.drawable.circle_up_transaction);
            holder.binding.textAmount.setTextColor(Color.parseColor("#68CA45"));
        } else {
            holder.binding.imageType.setImageResource(R.drawable.ic_down_transaction);
            holder.binding.imageType.setBackgroundResource(R.drawable.circle_down_transaction);
            holder.binding.textAmount.setTextColor(Color.parseColor("#FF5C5C"));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(data);
            }
        });

        // Buat ditahan lama
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onLongClick(data);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void setData(List<TransactionResponse.Data> data) {
        dataList.clear();
        dataList.addAll(data);
        notifyDataSetChanged();
    }

    public interface AdapterListener {
        void onClick(TransactionResponse.Data data);

        void onLongClick(TransactionResponse.Data data);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AdapterTransactionBinding binding;

        public ViewHolder(AdapterTransactionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}


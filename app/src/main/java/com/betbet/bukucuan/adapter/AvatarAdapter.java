package com.betbet.bukucuan.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.betbet.bukucuan.databinding.AdapterAvatarBinding;
import java.util.List;

public class AvatarAdapter extends RecyclerView.Adapter<AvatarAdapter.ViewHolder> {

    private final List<Integer> dataList;
    private final AdapterListener listener;

    public AvatarAdapter(List<Integer> dataList, AdapterListener listener) {
        this.dataList = dataList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AvatarAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(
                AdapterAvatarBinding
                        .inflate(LayoutInflater.from(parent.getContext()), parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull AvatarAdapter.ViewHolder holder, int position) {
        holder.binding.imageAvatar.setImageResource(dataList.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(dataList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void setData(List<Integer> data) {
        dataList.clear();
        dataList.addAll(data);
        notifyDataSetChanged();
    }

    public interface AdapterListener {
        void onClick(Integer avatar);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AdapterAvatarBinding binding;

        public ViewHolder(AdapterAvatarBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}

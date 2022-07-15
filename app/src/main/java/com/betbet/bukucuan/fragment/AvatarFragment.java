package com.betbet.bukucuan.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.betbet.bukucuan.R;
import com.betbet.bukucuan.adapter.AvatarAdapter;
import com.betbet.bukucuan.databinding.FragmentAvatarBinding;
import com.betbet.bukucuan.preferences.PreferencesManager;
import java.util.ArrayList;
import java.util.List;


public class AvatarFragment extends Fragment {

    private FragmentAvatarBinding binding;
    private AvatarAdapter avatarAdapter;
    private PreferencesManager pref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAvatarBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pref = new PreferencesManager(getContext());
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        List<Integer> avatars = new ArrayList<>();
        avatars.add(R.drawable.shiba);
        avatars.add(R.drawable.elephant);
        avatars.add(R.drawable.rabbit);
        avatars.add(R.drawable.dragon);
        avatars.add(R.drawable.tabby);
        avatars.add(R.drawable.tiger);
        avatarAdapter = new AvatarAdapter(avatars, new AvatarAdapter.AdapterListener() {
            @Override
            public void onClick(Integer avatar) {
                pref.put("pref_user_avatar", avatar);
                NavHostFragment.findNavController(AvatarFragment.this)
                        .navigateUp();
            }
        });
        binding.listAvatar.setAdapter(avatarAdapter);
    }
}


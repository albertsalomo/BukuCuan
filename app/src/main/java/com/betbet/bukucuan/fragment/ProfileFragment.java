package com.betbet.bukucuan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.betbet.bukucuan.R;
import com.betbet.bukucuan.activity.ChangePasswordActivity;
import com.betbet.bukucuan.activity.LoginActivity;
import com.betbet.bukucuan.databinding.FragmentProfileBinding;
import com.betbet.bukucuan.preferences.PreferencesManager;
import com.betbet.bukucuan.util.FormatUtil;

public class ProfileFragment extends Fragment {

    private PreferencesManager pref;
    private FragmentProfileBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pref = new PreferencesManager(getContext());
        setupListener();
    }

    @Override
    public void onStart() {
        super.onStart();
        getProfile();
    }

    private void setupListener() {
        binding.changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(), ChangePasswordActivity.class);
                startActivity(intent);
            }
        });


        binding.changeProfileIconButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ProfileFragment.this)
                        .navigate(R.id.action_profileFragment_to_avatarFragment);
            }
        });

        binding.cardLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pref.clear();
                Intent intent = new Intent(
                        requireActivity(), LoginActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                requireActivity().finish();
            }
        });
    }

    private void getProfile() {
        binding.imageAvatar.setImageResource(pref.getInteger("pref_user_avatar"));
        binding.labelUserName.setText(pref.getString("pref_user_name"));
        binding.labelEmail.setText(pref.getString("pref_user_email"));
        binding.labelJoinDate.setText(FormatUtil.dateProfile(pref.getString("pref_user_date")));
    }
}


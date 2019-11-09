package com.yakgwa.kullow.ui.goose;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.yakgwa.kullow.R;

public class GooseFragment extends Fragment {

    private GooseViewModel gooseViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        gooseViewModel =
                ViewModelProviders.of(this).get(GooseViewModel.class);
        View root = inflater.inflate(R.layout.fragment_goose_func, container, false);
        final TextView textView = root.findViewById(R.id.text_goose);
        gooseViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}

package com.yakgwa.kullow.ui.sign;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.yakgwa.kullow.ar.ImageTargets;


/**
 * kullow Building_Sign Recognition
 * - Image Target
 */
public class SignFragment extends Fragment{

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // Image Target Start
        Intent intent = new Intent(getActivity(), ImageTargets.class);
        startActivity(intent);


    }
}

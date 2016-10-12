package com.example.auro.puchoassignment.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.auro.puchoassignment.R;

/**
 * Created by stpl on 13/10/16.
 */

public class SignInFragment extends Fragment {

    public static final String TAG_SIGN_IN_FRAG = "SIGN_IN_FRAG";

    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_sign_in,container,true);
    }
}

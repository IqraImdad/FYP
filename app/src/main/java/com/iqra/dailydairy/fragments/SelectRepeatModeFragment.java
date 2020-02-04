package com.iqra.dailydairy.fragments;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.iqra.dailydairy.OnRepeatModeSelected;
import com.iqra.dailydairy.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectRepeatModeFragment extends DialogFragment {

    public static final String TAG = "ResetPasswordFragment";
    private View view;

    OnRepeatModeSelected mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_select_repeat_mode, container, false);
        setCancelable(true);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (OnRepeatModeSelected) context;
    }

    @Override
    public void onResume() {
//        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setLayout(getContext().getResources().getDimensionPixelSize(R.dimen.edit_text_width), WindowManager.LayoutParams.WRAP_CONTENT);
        super.onResume();
    }

    public static SelectRepeatModeFragment newInstance(String data) {

        Bundle bundle = new Bundle();
        bundle.putString("srb", data);
        SelectRepeatModeFragment fragment = new SelectRepeatModeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
}

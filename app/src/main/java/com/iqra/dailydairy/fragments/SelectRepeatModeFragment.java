package com.iqra.dailydairy.fragments;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.iqra.dailydairy.OnRepeatModeSelected;
import com.iqra.dailydairy.R;
import com.iqra.dailydairy.RepeatMode;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectRepeatModeFragment extends DialogFragment {

    public static final String TAG = "ResetPasswordFragment";
    private View view;
    private RadioGroup rgRepeatMode;
    private RadioButton rbOnce, rbMonth, rbYear;
    private OnRepeatModeSelected mListener;
    String repeatMode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_select_repeat_mode, container, false);
        setCancelable(true);

        rgRepeatMode = view.findViewById(R.id.rgRepeatMode);
        rbOnce = view.findViewById(R.id.rbOnce);
        rbMonth = view.findViewById(R.id.rbMonth);
        rbYear = view.findViewById(R.id.rbYear);

        Bundle bundle = getArguments();

        if (bundle != null)
            repeatMode = bundle.getString("srb");


        if (repeatMode.equalsIgnoreCase(RepeatMode.ONCE)) {
            rbOnce.setChecked(true);
        }
        if (repeatMode.equalsIgnoreCase(RepeatMode.MONTHLY)) {
            rbMonth.setChecked(true);
        }
        if (repeatMode.equalsIgnoreCase(RepeatMode.YEARLY)) {
            rbYear.setChecked(true);
        }

        rgRepeatMode.setOnCheckedChangeListener((radioGroup, i) -> {
            RadioButton rb = radioGroup.findViewById(i);
            if (rb.isChecked()) {
                if (rb == rbOnce) {
                    repeatMode = RepeatMode.ONCE;
                }
                if (rb == rbMonth) {
                    repeatMode = RepeatMode.MONTHLY;
                }
                if (rb == rbYear) {
                    repeatMode = RepeatMode.YEARLY;
                }
            }
        });

        view.findViewById(R.id.btnSetRepeatMode).setOnClickListener(view -> {
            mListener.onRepeatModeSelected(repeatMode);
            dismiss();
        });

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

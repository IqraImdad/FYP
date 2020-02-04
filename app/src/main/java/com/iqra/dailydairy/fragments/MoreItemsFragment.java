package com.iqra.dailydairy.fragments;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iqra.dailydairy.CreateEventActivity;
import com.iqra.dailydairy.MainActivity;
import com.iqra.dailydairy.OnItemClicked;
import com.iqra.dailydairy.R;
import com.iqra.dailydairy.adapters.MoreItemAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoreItemsFragment extends DialogFragment {

    public static final String TAG = "ResetPasswordFragment";
    private View view;
    private RecyclerView rvMoreItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_more_items, container, false);


        buildRecyclerView();

        setCancelable(true);

        return view;
    }

    private void buildRecyclerView() {
        rvMoreItems = view.findViewById(R.id.rvMoreItems);
        rvMoreItems.setLayoutManager(new LinearLayoutManager(requireContext()));
        MoreItemAdapter adapter = new MoreItemAdapter(MainActivity.moreItems);
        rvMoreItems.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClicked() {
            @Override
            public void onItemClicked(int position) {
                dismiss();
                Intent intent = new Intent(requireContext(), CreateEventActivity.class);
                intent.putExtra("id",String.valueOf(MainActivity.moreItems.get(position).getId()));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
//        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setLayout(getContext().getResources().getDimensionPixelSize(R.dimen.edit_text_width), WindowManager.LayoutParams.WRAP_CONTENT);
        super.onResume();
    }
}

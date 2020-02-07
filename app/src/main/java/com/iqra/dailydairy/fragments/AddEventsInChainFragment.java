package com.iqra.dailydairy.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iqra.dailydairy.ChainsEventActivity;
import com.iqra.dailydairy.Event;
import com.iqra.dailydairy.R;
import com.iqra.dailydairy.adapters.NewChainEventsAdapter;
import com.iqra.dailydairy.room.EventDao;
import com.iqra.dailydairy.room.MyRoomDatabase;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddEventsInChainFragment extends DialogFragment {

    public static final String TAG = "ResetPasswordFragment";
    private View view;
    private RecyclerView rv;
    private ArrayList<Event> events;
    private ArrayList<Event> selectedEvents;
    private TextView tvND;

    private static OnEventsSelected mListener;

    public interface OnEventsSelected {
        void onEventsSelected(ArrayList<Event> events);
    }

    public static AddEventsInChainFragment newInstance(OnEventsSelected listener) {
        mListener = listener;
        return new AddEventsInChainFragment();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_event_in_chain, container, false);
        EventDao eventDao = MyRoomDatabase.getDatabase(requireContext()).eventDao();
        events = (ArrayList<Event>) eventDao.getAllEvents();
        setCancelable(true);

        selectedEvents = new ArrayList<>();
        for (Event e : ChainsEventActivity.currentEvents) {
            events.remove(e);
        }

        initComponents();
        buildRecyclerView();

        setCancelable(true);

        return view;
    }

    private void initComponents() {
        rv = view.findViewById(R.id.rv);
        Button btnDone = view.findViewById(R.id.btnDone);
        tvND = view.findViewById(R.id.tvND);
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onEventsSelected(selectedEvents);
                dismiss();
            }
        });
    }

    private void buildRecyclerView() {

        if (events.size() < 1) {
            tvND.setVisibility(View.VISIBLE);
            rv.setAdapter(null);
        } else {
            tvND.setVisibility(View.GONE);
            rv.setLayoutManager(new LinearLayoutManager(requireContext()));

            NewChainEventsAdapter adapter = new NewChainEventsAdapter(events);
            adapter.setCheckBoxClickListener((isChecked, position) -> {
                if (isChecked) {
                    selectedEvents.add(events.get(position));
                } else {
                    selectedEvents.remove(events.get(position));
                }
            });
            rv.setAdapter(adapter);
        }

    }

    @Override
    public void onResume() {
//        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        super.onResume();
    }
}

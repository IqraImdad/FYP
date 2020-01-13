package com.iqra.dailydairy;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iqra.dailydairy.adapters.ChainsEventsAdapter;

import java.util.Calendar;

public class ChainsEventActivity extends AppCompatActivity {

    RecyclerView rvChainsEvent;
    String selectedYear, selectedDay, selectedMonth;
    DatePickerDialog picker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chains_event);

        initComponents();
        buildRecyclerView();
    }

    private void buildRecyclerView() {
        rvChainsEvent.setLayoutManager(new LinearLayoutManager(this));
        ChainsEventsAdapter adapter = new ChainsEventsAdapter(ChainActivity.selectedChainsEvents);
        rvChainsEvent.setAdapter(adapter);
        adapter.setOnClickListener(new OnItemClicked() {
            @Override
            public void onItemClicked(int position) {
                showDatePicker();
            }
        });
    }


    private void initComponents() {
        rvChainsEvent = findViewById(R.id.rvChainsEvents);

    }


    private void showDatePicker() {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);

        // date picker dialog
        picker = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        monthOfYear++;
                        selectedMonth = String.valueOf(monthOfYear);
                        selectedYear = String.valueOf(year);
                        selectedDay = String.valueOf(dayOfMonth);

//                        btnSelectDate.setText("Selected Date:  " + dayOfMonth + "-" + monthOfYear + "-" + year);
                    }
                }, year, month, day);
        picker.show();

    }

}

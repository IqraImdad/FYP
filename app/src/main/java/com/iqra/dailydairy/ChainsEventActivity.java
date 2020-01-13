package com.iqra.dailydairy;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iqra.dailydairy.adapters.ChainsEventsAdapter;
import com.iqra.dailydairy.room.ChainDao;
import com.iqra.dailydairy.room.MyRoomDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.iqra.dailydairy.ChainActivity.*;

public class ChainsEventActivity extends AppCompatActivity {

    RecyclerView rvChainsEvent;
    String selectedYear, selectedDay, selectedMonth;
    DatePickerDialog picker;
    ChainsEventsAdapter adapter;
    ChainDao chainDao;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chains_event);
        chainDao =  MyRoomDatabase.getDatabase(this).chainDao();
        id = getIntent().getStringExtra("id");
        initComponents();
        buildRecyclerView();
    }

    private void buildRecyclerView() {
        rvChainsEvent.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ChainsEventsAdapter(selectedChainsEvents);
        rvChainsEvent.setAdapter(adapter);
        adapter.setOnClickListener(new OnItemClicked() {
            @Override
            public void onItemClicked(int position) {
                showDatePicker(position);
            }
        });
    }


    private void initComponents() {
        rvChainsEvent = findViewById(R.id.rvChainsEvents);

    }


    private void showDatePicker(int pos) {
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
                        int difDays = getDifferenceOfDays(pos);

                        updateEvents(difDays);


//                        btnSelectDate.setText("Selected Date:  " + dayOfMonth + "-" + monthOfYear + "-" + year);
                    }
                }, year, month, day);
        picker.show();

    }

    private void updateEvents(int days) {
        Calendar calendar = Calendar.getInstance();

        for (int i = 0; i < selectedChainsEvents.size(); i++) {
            Event e = selectedChainsEvents.get(i);
            calendar.set(Integer.parseInt(e.getYear()), Integer.parseInt(e.getMonth()), Integer.parseInt(e.getDay()));
            calendar.add(Calendar.DAY_OF_MONTH ,days);

            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

            selectedChainsEvents.get(i).setYear(String.valueOf(year));
            selectedChainsEvents.get(i).setMonth(String.valueOf(month));
            selectedChainsEvents.get(i).setDay(String.valueOf(day));
        }

        chainDao.update(selectedChainsEvents,id);
        adapter.notifyDataSetChanged();
        Chain c =  chainDao.getChains(id);
        selectedChainsEvents.clear();
        selectedChainsEvents.addAll(c.getEvents());
    }


    private int getDifferenceOfDays(int pos) {

        int days = -1;
        Event event = selectedChainsEvents.get(pos);

        SimpleDateFormat myFormat = new SimpleDateFormat("dd MM yyyy");
        String inputString1 = selectedDay + " " + selectedMonth + " " + selectedYear;
        String inputString2 = event.getDay() + " " + event.getMonth() + " " + event.getYear();

        try {
            Date date1 = myFormat.parse(inputString1);
            Date date2 = myFormat.parse(inputString2);
            long diff = date1.getTime() - date2.getTime();
            days = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
            Log.d("TAG", "getDifferenceOfDays: " + days + "");
//            System.out.println("Days: " +);
        } catch (ParseException e) {
            e.printStackTrace();
            return days;
        }

        return days;
    }
}

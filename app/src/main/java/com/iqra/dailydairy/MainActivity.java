package com.iqra.dailydairy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.base.Optional;
import com.google.common.collect.FluentIterable;
import com.iqra.dailydairy.adapters.EventsAdapter;
import com.iqra.dailydairy.room.EventDao;
import com.iqra.dailydairy.room.MyRoomDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton btnNextMonth, btnPreviousMonth;
    Button btnAdd, btnChain;
    TextView tvSelectedMonth;
    Calendar calendar;
    Date currentDate;
    EventDao eventDao;
    RecyclerView rvEvents;
    EventsAdapter mAdapter;
    HashMap<String, Event> eventsMap = new HashMap<>();
    ArrayList<Event> eventsList = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        eventDao = MyRoomDatabase.getDatabase(this).eventDao();
        initComponents();

        calendar = Calendar.getInstance();
        currentDate = calendar.getTime();
//        tvSelectedMonth.setText(formatDate(currentDate));
    }

    @Override
    protected void onResume() {
        super.onResume();
        tvSelectedMonth.setText(formatDate(currentDate));
        buildRecyclerView(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
    }

    private void initComponents() {
        btnNextMonth = findViewById(R.id.btnNextMonth);
        btnPreviousMonth = findViewById(R.id.btnLastMonth);
        btnAdd = findViewById(R.id.btnAdd);
        btnChain = findViewById(R.id.btnChain);
        btnNextMonth.setOnClickListener(this);
        btnPreviousMonth.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnChain.setOnClickListener(this);

        tvSelectedMonth = findViewById(R.id.tvSelectedMonth);
        rvEvents = findViewById(R.id.rvEvents);

    }

    private void buildRecyclerView(int _maxDays) {
        ArrayList<String> days = new ArrayList<>();
        eventsMap.clear();
        for (int i = 1; i <= _maxDays; i++) {
            days.add(String.valueOf(i));
            Event event = findByDay(eventsList, String.valueOf(i));
            if (event != null) {
                eventsMap.put(String.valueOf(i), event);
            }
        }


        rvEvents.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new EventsAdapter(days, eventsMap);
        rvEvents.setAdapter(mAdapter);
        rvEvents.scrollToPosition(calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onClick(View v) {
        if (v == btnNextMonth) {
            nextMonth();
        }
        if (v == btnPreviousMonth) {
            previousMonth();
        }
        if (v == btnAdd) {
            startActivity(new Intent(this, CreateEventActivity.class));
        }
        if (v == btnChain) {
            startActivity(new Intent(this, ChainActivity.class));
        }
    }

    private void nextMonth() {
        calendar.add(Calendar.MONTH, 1);
        currentDate = calendar.getTime();
        tvSelectedMonth.setText(formatDate(currentDate));
        buildRecyclerView(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
    }

    private void previousMonth() {
        calendar.add(Calendar.MONTH, -1);
        currentDate = calendar.getTime();
        tvSelectedMonth.setText(formatDate(currentDate));
        buildRecyclerView(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
    }

    private String formatDate(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("MMM-yyyy");
        String selectedDate = df.format(date);
        String selectedYear = selectedDate.split("-")[1];

        SimpleDateFormat df2 = new SimpleDateFormat("MM-yyyy");
        String selectedDate2 = df2.format(date);
        String selectedMonth = selectedDate2.split("-")[0];

        eventsList = (ArrayList<Event>) eventDao.getEventsOfMonth(selectedMonth, selectedYear);
        ArrayList<Event> lst = (ArrayList<Event>) eventDao.getAllEvents();


        return selectedDate;
    }

    private Event findByDay(List<Event> userList, final String _day) {
        Optional<Event> userOptional =
                FluentIterable.from(userList).firstMatch(input -> input.getDay().equalsIgnoreCase(_day));
        return userOptional.isPresent() ? userOptional.get() : null; // return user if found otherwise return null if user name don't exist in user list
    }
}
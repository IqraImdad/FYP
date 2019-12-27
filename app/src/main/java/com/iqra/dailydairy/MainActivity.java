package com.iqra.dailydairy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iqra.dailydairy.adapters.EventsAdapter;
import com.iqra.dailydairy.room.EventDao;
import com.iqra.dailydairy.room.WordRoomDatabase;

import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton btnNextMonth, btnPreviousMonth;
    Button btnAdd;
    TextView tvSelectedMonth;
    Calendar calendar;
    Date currentDate;
    EventDao eventDao;
    RecyclerView rvEvents;
    EventsAdapter mAdapter;
    ArrayList<Event> eventsList = new ArrayList<>();
    int maxDays;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        eventDao = WordRoomDatabase.getDatabase(this).eventDao();
        initComponents();

        calendar = Calendar.getInstance();
        currentDate = calendar.getTime();
        tvSelectedMonth.setText(formatDate(currentDate));

        buildRecyclerView(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
    }

    private void initComponents() {
        btnNextMonth = findViewById(R.id.btnNextMonth);
        btnPreviousMonth = findViewById(R.id.btnLastMonth);
        btnAdd = findViewById(R.id.btnAdd);
        btnNextMonth.setOnClickListener(this);
        btnPreviousMonth.setOnClickListener(this);
        btnAdd.setOnClickListener(this);

        tvSelectedMonth = findViewById(R.id.tvSelectedMonth);
        rvEvents = findViewById(R.id.rvEvents);

    }

    private void buildRecyclerView(int _maxdays) {

        rvEvents.setLayoutManager(new LinearLayoutManager(this));
        rvEvents.setHasFixedSize(true);

        mAdapter = new EventsAdapter(_maxdays,eventsList);
        rvEvents.setAdapter(mAdapter);


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
    }

    private void nextMonth() {
        calendar.add(Calendar.MONTH, 1);
        currentDate = calendar.getTime();
        tvSelectedMonth.setText(formatDate(currentDate));
        maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        buildRecyclerView(maxDays);

    }

    private void previousMonth() {
        calendar.add(Calendar.MONTH, -1);
        currentDate = calendar.getTime();
        tvSelectedMonth.setText(formatDate(currentDate));
        maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        buildRecyclerView(maxDays);
    }

    private String formatDate(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("MMM-yyyy");
        String selectedDate = df.format(date);
        String selectedMonth = selectedDate.split("-")[0];
        String selectedYear = selectedDate.split("-")[1];

        SimpleDateFormat df2 = new SimpleDateFormat("MM-yyyy");
        String selectedDate2 = df2.format(date);
        String selectedMonth2 = selectedDate2.split("-")[0];

        eventsList = (ArrayList<Event>) eventDao.getEventsOfMonth(selectedMonth2, selectedYear);

        Toast.makeText(this,  eventDao.getEventsOfMonth(selectedMonth, selectedYear).size()+"", Toast.LENGTH_SHORT).show();
        return selectedDate;
    }
}
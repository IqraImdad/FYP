package com.iqra.dailydairy;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.iqra.dailydairy.fragments.MoreItemsFragment;
import com.iqra.dailydairy.room.EventDao;
import com.iqra.dailydairy.room.MyRoomDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    ImageButton btnNextMonth, btnPreviousMonth;
    Button btnAdd, btnChain;
    TextView tvSelectedMonth;
    String todaysMonth;
    Calendar calendar;
    Date currentDate;
    EventDao eventDao;
    RecyclerView rvEvents;
    EventsAdapter mAdapter;
    HashMap<String, Event> eventsMap = new HashMap<>();
    public static ArrayList<Event> moreItems = new ArrayList<>();
    ArrayList<Event> eventsList = new ArrayList<>();

    Boolean isCurrentMonth = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        eventDao = MyRoomDatabase.getDatabase(this).eventDao();
        initComponents();

        calendar = Calendar.getInstance();
        currentDate = calendar.getTime();

        SimpleDateFormat df2 = new SimpleDateFormat("MM-yyyy", Locale.getDefault());
        String selectedDate2 = df2.format(currentDate);
        todaysMonth = selectedDate2.split("-")[0];
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

        tvSelectedMonth.setOnClickListener(view -> showDatePicker());

    }

    private void showDatePicker() {
        DatePickerDialog picker;

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        // date picker dialog
        picker = new DatePickerDialog(MainActivity.this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    monthOfYear++;
                    calendar.set(year1, monthOfYear - 1, dayOfMonth);
                    currentDate = calendar.getTime();
                    tvSelectedMonth.setText(formatDate(currentDate));
                    buildRecyclerView(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                }, year, month, day);
        picker.show();

    }


    private void buildRecyclerView(int _maxDays) {
        ArrayList<String> days = new ArrayList<>();
        eventsMap.clear();
        for (int i = 1; i <= _maxDays; i++) {

            String index = String.valueOf(i);

            days.add(index);
            Event event = findByDay(eventsList, index);
            if (event != null) {

                if (!event.getRepeatMode().equalsIgnoreCase(RepeatMode.ONCE) && isDateAfterNow(event.getDate())) {
                    updateEvent(event);
                    continue;
                }

                DateFormat dateFormat = new SimpleDateFormat("yyyy MM d hh mm", Locale.getDefault());
                String strDate = dateFormat.format(event.getDate());
                String[] date = strDate.split(" ");
                ArrayList<Event> eventsOfDays = (ArrayList<Event>) eventDao.getEventsOfDay(date[0], date[1], date[2]);

                if (eventsOfDays.size() > 1) {
                    event.setMoreThenOne(true);
                    for (int k = 0; k < eventsOfDays.size(); k++) {
                        if (!eventsOfDays.get(k).getRepeatMode().equalsIgnoreCase(RepeatMode.ONCE) && isDateAfterNow(eventsOfDays.get(k).getDate())) {
                            updateEvent(eventsOfDays.get(k));
                        }
                    }

                }


                eventsMap.put(String.valueOf(i), event);
            }
        }

        rvEvents.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new EventsAdapter(days, eventsMap, isCurrentMonth);
        rvEvents.setAdapter(mAdapter);
        rvEvents.scrollToPosition(calendar.get(Calendar.DAY_OF_MONTH) - 1);
        mAdapter.setOnMoreEventClickedListener(date -> {
            DateFormat dateFormat = new SimpleDateFormat("yyyy MM d", Locale.getDefault());
            String strDate = dateFormat.format(date);
            String[] d = strDate.split(" ");
            moreItems = (ArrayList<Event>) eventDao.getEventsOfDay(d[0], d[1], d[2]);
            new MoreItemsFragment().show(getSupportFragmentManager(), "");
            Log.d("TAG", "buildRecyclerView: " + strDate);
        });


    }

    private void updateEvent(Event event) {
        String repeatMode = event.getRepeatMode();


        Date d = event.getDate();
        Calendar c = Calendar.getInstance();
        c.setTime(d);

        if (repeatMode.equalsIgnoreCase(RepeatMode.MONTHLY)) {
            c.add(Calendar.MONTH, 1);
        } else {
            c.add(Calendar.DAY_OF_YEAR, 1);
        }

        d = c.getTime();

        DateFormat dateFormat = new SimpleDateFormat("yyyy MM d", Locale.getDefault());
        String strDate = dateFormat.format(d);
        String[] d2 = strDate.split(" ");
        event.setYear(d2[0]);
        event.setMonth(d2[1]);
        event.setDay(d2[2]);
        dateFormat = new SimpleDateFormat("hh:mm", Locale.getDefault());
        String time = dateFormat.format(d);
        event.setTime(time);
        eventDao.updateEvent(event);

    }

    private Boolean isDateAfterNow(Date date) {
        // Get Current Date Time
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM d hh mm", Locale.getDefault());
        String getCurrentDateTime = sdf.format(c.getTime());
        String getMyTime = sdf.format(date);

        Log.d("TAG", "isDateAfterNow: "+getCurrentDateTime);
        Log.d("TAG", "isDateAfterNow: "+getMyTime);
        return getCurrentDateTime.compareTo(getMyTime) > 0;
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
        SimpleDateFormat df = new SimpleDateFormat("MMM-yyyy", Locale.getDefault());
        String selectedDate = df.format(date);
        String selectedYear = selectedDate.split("-")[1];

        //to show on main screen
        SimpleDateFormat df2 = new SimpleDateFormat("MM-yyyy", Locale.getDefault());
        String selectedDate2 = df2.format(date);
        String selectedMonth = selectedDate2.split("-")[0];

        isCurrentMonth = todaysMonth.equalsIgnoreCase(selectedMonth);

        eventsList = (ArrayList<Event>) eventDao.getEventsOfMonth(selectedMonth, selectedYear);

        return selectedDate;
    }

    private Event findByDay(List<Event> userList, final String _day) {
        Optional<Event> userOptional =
                FluentIterable.from(userList).firstMatch(input -> input.getDay().equalsIgnoreCase(_day));
        return userOptional.isPresent() ? userOptional.get() : null; // return user if found otherwise return null if user name don't exist in user list
    }
}
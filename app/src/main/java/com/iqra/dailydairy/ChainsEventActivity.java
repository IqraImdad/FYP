package com.iqra.dailydairy;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iqra.dailydairy.adapters.ChainsEventsAdapter;
import com.iqra.dailydairy.room.ChainDao;
import com.iqra.dailydairy.room.EventDao;
import com.iqra.dailydairy.room.MyRoomDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class ChainsEventActivity extends AppCompatActivity {

    RecyclerView rvChainsEvent;
    String selectedYear, selectedDay, selectedMonth;
    String fromSelectedYear, fromSelectedDay, fromSelectedMonth;
    String toSelectedYear, toSelectedDay, toSelectedMonth;
    ChainsEventsAdapter adapter;
    ArrayList<Event> events = new ArrayList<>();
    Chain chain = new Chain();
    DatePickerDialog picker;
    String id = "";
    ChainDao chainDao;
    EventDao eventDao;
    RadioGroup rgEvents;
    LinearLayout llFT;
    TextView tvFrom, tvNDI, tvNoEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chains_event);
        chainDao = MyRoomDatabase.getDatabase(this).chainDao();
        eventDao = MyRoomDatabase.getDatabase(this).eventDao();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = String.valueOf(extras.getInt("id"));
        }

        initComponents();
        getAllEvents();
        sortEvents();
        buildRecyclerView();
    }

    private void getAllEvents() {
        events.clear();
        chain = chainDao.getChains(id);
        ArrayList<Event> tempEvents = chain.getEvents();
        for (int i = 0; i < tempEvents.size(); i++) {
            Event event = eventDao.getEvent(String.valueOf(tempEvents.get(i).getId()));
            if (event != null) {
                events.add(eventDao.getEvent(String.valueOf(tempEvents.get(i).getId())));
            }
        }
    }

    private void buildRecyclerView() {
        if (events.size() < 1) {
            tvNoEvents.setVisibility(View.VISIBLE);
            rvChainsEvent.setAdapter(null);
        } else {
            tvNoEvents.setVisibility(View.GONE);
            rvChainsEvent.setLayoutManager(new LinearLayoutManager(this));
            adapter = new ChainsEventsAdapter(events);
            rvChainsEvent.setAdapter(adapter);
            adapter.setOnClickListener(this::showDatePicker);
        }


    }


    private void initComponents() {
        rvChainsEvent = findViewById(R.id.rvChainsEvents);
        rgEvents = findViewById(R.id.rgEvents);
        llFT = findViewById(R.id.llFT);
        tvFrom = findViewById(R.id.tvFrom);
        tvNDI = findViewById(R.id.tvNoDI);
        tvNoEvents = findViewById(R.id.tvNoEvents);

        llFT.setOnClickListener(view -> showDatePickerForFromDate());

        rgEvents.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.rbAllEvents) {
                RadioButton btn = radioGroup.findViewById(R.id.rbAllEvents);
                if (btn.isChecked()) {
                    llFT.setVisibility(View.GONE);
                    getAllEvents();
                    sortEvents();
                    buildRecyclerView();
                    tvNDI.setVisibility(View.GONE);
                }
            } else {
                RadioButton btn = radioGroup.findViewById(R.id.rbRange);
                if (btn.isChecked()) {
                    showNoIntervalMsg();
                    showDatePickerForFromDate();
                    llFT.setVisibility(View.VISIBLE);

                }
            }
        });

    }

    private void showDatePickerForFromDate() {

        DatePickerDialog mPicker;
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        Toast.makeText(this, "Select Event Starting Date", Toast.LENGTH_SHORT).show();
        // date picker dialog
        mPicker = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> {

                    fromSelectedDay = String.valueOf(dayOfMonth);
                    fromSelectedMonth = String.valueOf(monthOfYear + 1);
                    fromSelectedYear = String.valueOf(year1);

                    showDatePickerForToDate();

                }, year, month, day);

        mPicker.setOnCancelListener(dialogInterface -> showNoIntervalMsg());

        mPicker.setTitle("From");
        mPicker.setCancelable(false);
        mPicker.show();

    }

    private void showNoIntervalMsg() {
        rvChainsEvent.setAdapter(null);
        tvNDI.setVisibility(View.VISIBLE);

    }

    private void showDatePickerForToDate() {
        Toast.makeText(this, "Select Event Ending Date", Toast.LENGTH_SHORT).show();
        DatePickerDialog mPicker;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Integer.valueOf(fromSelectedYear), Integer.valueOf(fromSelectedMonth) - 1, Integer.valueOf(fromSelectedDay));
        // date picker dialog
        calendar.setFirstDayOfWeek(Integer.valueOf(fromSelectedDay));
        calendar.setFirstDayOfWeek(Integer.valueOf(fromSelectedDay));
        calendar.setFirstDayOfWeek(Integer.valueOf(fromSelectedDay));

        mPicker = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> {

                    toSelectedDay = String.valueOf(dayOfMonth);
                    toSelectedMonth = String.valueOf(monthOfYear + 1);
                    toSelectedYear = String.valueOf(year1);

                    String from = fromSelectedDay + "-" + fromSelectedMonth + "-" + fromSelectedYear;
                    String to = toSelectedDay + "-" + toSelectedMonth + "-" + toSelectedYear;
                    tvFrom.setText(from + " to " + to);

                    ArrayList<Event> e = getNewList((ArrayList<Event>) eventDao.getAllEvents(), from, to);
                    events.clear();
                    events.addAll(e);

                    buildRecyclerView();


                }, Integer.valueOf(fromSelectedYear), Integer.valueOf(fromSelectedMonth) - 1, Integer.valueOf(fromSelectedDay));

        mPicker.setOnCancelListener(dialogInterface -> showNoIntervalMsg());

        mPicker.setTitle("To");
        mPicker.getDatePicker().setMinDate(calendar.getTimeInMillis());
        mPicker.setCancelable(false);
        mPicker.show();

    }


    private void showDatePicker(int pos) {
        int day = Integer.valueOf(events.get(0).getDay());
        int month = Integer.valueOf(events.get(0).getMonth());
        int year = Integer.valueOf(events.get(0).getYear());

        // date picker dialog
        picker = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> {

                    monthOfYear++;
                    selectedMonth = String.valueOf(monthOfYear);
                    selectedYear = String.valueOf(year1);
                    selectedDay = String.valueOf(dayOfMonth);
                    int difDays = getDifferenceOfDays(pos);
                    Log.d("TAg", "showDatePicker: " + difDays + "");
                    updateEvents(difDays);
//                  btnSelectDate.setText("Selected Date:  " + dayOfMonth + "-" + monthOfYear + "-" + year);
                }, year, month - 1, day);
        picker.show();

    }

    private void updateEvents(int days) {
        Calendar calendar = Calendar.getInstance();

        for (int i = 0; i < events.size(); i++) {
            Event e = events.get(i);
            calendar.set(Integer.parseInt(e.getYear()), Integer.parseInt(e.getMonth()) - 1, Integer.parseInt(e.getDay()));
            calendar.add(Calendar.DAY_OF_MONTH, days);

            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            month++;
            Log.d("Tag", "selectedmonth: " + month + "");
            int year = calendar.get(Calendar.YEAR);

            String mMonth = String.valueOf(month);
            if (mMonth.length() < 2)
                mMonth = "0" + mMonth;

            events.get(i).setYear(String.valueOf(year));
            events.get(i).setMonth(mMonth);
            events.get(i).setDay(String.valueOf(day));

            eventDao.updateEvent(events.get(i));
        }

        chainDao.update(events, id);
        Chain c = chainDao.getChains(id);
        events.clear();
        events.addAll(c.getEvents());
        sortEvents();
        adapter.notifyDataSetChanged();

    }

    private ArrayList<Event> getNewList(ArrayList<Event> oldList, String from, String to) {
        ArrayList<Event> newList = new ArrayList<Event>();
        Date d1 = null, d2 = null, d3 = null, d4 = null;
        SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);

        try {
            d3 = f.parse(from);
            d4 = f.parse(to);
            for (int i = 0; i < oldList.size(); i++) {

                String b = oldList.get(i).getlastaction();

                d2 = f.parse(b);

                if (d2.compareTo(d3) >= 0 && d2.compareTo(d4) <= 0) {
                    newList.add(oldList.get(i));
                }
            }
        } catch (Exception ex) {

        }
        return newList;
    }

    private void sortEvents() {
        if (events.size() > 1) {
            Collections.sort(events, new Comparator<Event>() {
                public int compare(Event o1, Event o2) {
                    if (o1.getDate() == null || o2.getDate() == null)
                        return 0;
                    return o1.getDate().compareTo(o2.getDate());
                }
            });
        }
    }


    private int getDifferenceOfDays(int pos) {

        int days = -1;
        Event event = events.get(pos);
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

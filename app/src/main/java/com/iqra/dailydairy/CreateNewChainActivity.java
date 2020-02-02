package com.iqra.dailydairy;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iqra.dailydairy.adapters.NewChainEventsAdapter;
import com.iqra.dailydairy.room.ChainDao;
import com.iqra.dailydairy.room.EventDao;
import com.iqra.dailydairy.room.MyRoomDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class CreateNewChainActivity extends AppCompatActivity implements OnCheckBoxClicked, View.OnClickListener {

    RecyclerView rvEvents;
    Button btnSaveChain;
    EditText etChainName;
    EventDao eventDao;
    ChainDao chainDao;
    ArrayList<Event> selectedEvents;
    ArrayList<Event> events;
    RadioGroup rgEvents;
    LinearLayout llFT;
    String fromSelectedYear, fromSelectedDay, fromSelectedMonth;
    String toSelectedYear, toSelectedDay, toSelectedMonth;
    TextView tvFrom, tvNoEvents;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_chain);

        events = new ArrayList<>();
        eventDao = MyRoomDatabase.getDatabase(this).eventDao();
        chainDao = MyRoomDatabase.getDatabase(this).chainDao();
        selectedEvents = new ArrayList<>();
        initComponents();
        events = (ArrayList<Event>) eventDao.getAllEvents();
        buildRecyclerView();

    }

    private void initComponents() {
        rvEvents = findViewById(R.id.rvEvents);
        btnSaveChain = findViewById(R.id.btnSaveChain);
        etChainName = findViewById(R.id.etChainName);
        tvNoEvents = findViewById(R.id.tvNd);
        btnSaveChain.setOnClickListener(this);

        rgEvents = findViewById(R.id.rgEvents);
        llFT = findViewById(R.id.llFT);
        tvFrom = findViewById(R.id.tvFrom);

        llFT.setOnClickListener(view -> showDatePickerForFromDate());

        rgEvents.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.rbAllEvents) {
                RadioButton btn = radioGroup.findViewById(R.id.rbAllEvents);
                if (btn.isChecked()) {
                    llFT.setVisibility(View.GONE);
                    getAllEvents();
                    sortEvents();
                    buildRecyclerView();
                }
            } else {
                selectedEvents.clear();
                RadioButton btn = radioGroup.findViewById(R.id.rbRange);
                if (btn.isChecked()) {
                    showNoIntervalMsg();
                    showDatePickerForFromDate();
                    llFT.setVisibility(View.VISIBLE);
                }
            }
        });

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

    private void getAllEvents() {
        events.clear();
        events.addAll(eventDao.getAllEvents());
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
        rvEvents.setAdapter(null);
    }

    private void buildRecyclerView() {

        if (events.size() < 1) {
            tvNoEvents.setVisibility(View.VISIBLE);
            return;
        } else {
            tvNoEvents.setVisibility(View.GONE);
        }

        rvEvents.setLayoutManager(new LinearLayoutManager(this));

        NewChainEventsAdapter adapter = new NewChainEventsAdapter(events);
        adapter.setCheckBoxClickListener(this);
        rvEvents.setAdapter(adapter);

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

    @Override
    public void onCheckBoxClicked(boolean isChecked, int position) {
        if (isChecked) {
            selectedEvents.add(events.get(position));
        } else {
            selectedEvents.remove(events.get(position));
        }
    }

    @Override
    public void onClick(View view) {
        if (view == btnSaveChain) {
            if (selectedEvents.size() < 2) {
                Toast.makeText(this, "Please Select more then one Events", Toast.LENGTH_SHORT).show();

            } else if (etChainName.getText().toString().trim().equalsIgnoreCase("")) {
                etChainName.setError("Name Required");
            } else {
                Chain chain = new Chain();
                chain.setName(etChainName.getText().toString().trim());
                chain.setEvents(selectedEvents);
                chainDao.insert(chain);
                Toast.makeText(this, "Chain saved", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

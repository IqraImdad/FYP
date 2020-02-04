package com.iqra.dailydairy;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.iqra.dailydairy.fragments.SelectRepeatModeFragment;
import com.iqra.dailydairy.room.EventDao;
import com.iqra.dailydairy.room.MyRoomDatabase;

import java.util.Calendar;

public class CreateEventActivity extends AppCompatActivity implements View.OnClickListener , OnRepeatModeSelected {

    LinearLayout btnSelectRepeatMode;
    TextView btnSelectDate, btnSelectTime, tvRepeatMode;
    Button btnSaveEvent, btnDeleteEvent;
    EditText etEvenName, etVenue, etNote;
    DatePickerDialog picker;
    String selectedYear, selectedDay, selectedMonth, selectedTime = "";
    EventDao eventDao;
    private boolean isUpdating = false;
    String eventId = "";
    Event updatingEvent;
    String repeatMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        eventDao = MyRoomDatabase.getDatabase(this).eventDao();
        initComponents();

        repeatMode = RepeatMode.ONCE;

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            eventId = bundle.getString("id");
            isUpdating = true;
            updatingEvent = eventDao.getEvent(eventId);
            selectedYear = updatingEvent.getYear();
            selectedMonth = updatingEvent.getMonth();
            selectedDay = updatingEvent.getDay();
            selectedTime = updatingEvent.getTime();
            setSelectedTime();
            setSelectedDate();
            etEvenName.setText(updatingEvent.getName());
            etNote.setText(updatingEvent.getNote());
            etVenue.setText(updatingEvent.getVenue());
            btnSaveEvent.setText("Update");
            btnDeleteEvent.setVisibility(View.VISIBLE);
        } else {
            btnDeleteEvent.setVisibility(View.GONE);
            Calendar calendar = Calendar.getInstance();
            selectedYear = String.valueOf(calendar.get(Calendar.YEAR));
            selectedDay = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
            selectedMonth = String.valueOf(calendar.get(Calendar.MONTH) + 1);
            setSelectedDate();

        }
    }

    private void setSelectedDate() {

        btnSelectDate.setText("Selected Date:  " + selectedDay + "-" + selectedMonth + "-" + selectedYear);

    }

    private void setSelectedTime() {
        if (!selectedTime.equalsIgnoreCase("")) {
            btnSelectTime.setText("selected Time: " + selectedTime);
        }
    }


    private void initComponents() {
        btnSelectDate = findViewById(R.id.btnSelectDate);
        btnSelectTime = findViewById(R.id.btnSelectTime);
        btnSaveEvent = findViewById(R.id.btnSaveEvent);
        btnDeleteEvent = findViewById(R.id.btnDelete);
        btnSelectRepeatMode = findViewById(R.id.btnSelectRepeatMode);
        tvRepeatMode = findViewById(R.id.tvRepeatMode);

        btnSelectRepeatMode.setOnClickListener(this);
        btnSelectDate.setOnClickListener(this);
        btnDeleteEvent.setOnClickListener(this);
        btnSelectTime.setOnClickListener(this);
        btnSaveEvent.setOnClickListener(this);
        etEvenName = findViewById(R.id.etEventName);
        etVenue = findViewById(R.id.etVenue);
        etNote = findViewById(R.id.etNote);


    }

    @Override
    public void onClick(View v) {
        if (v == btnSelectDate) {
            showDatePicker();
        }
        if (v == btnSelectTime) {
            showTimePicker();
        }
        if (v == btnSaveEvent) {
            if (isFormValid()) {
                Event event = new Event();
                event.setName(etEvenName.getText().toString().trim());
                event.setVenue(etVenue.getText().toString().trim());
                event.setNote(etNote.getText().toString().trim());
                event.setDay(selectedDay);

                if (selectedMonth.length() < 2)
                    selectedMonth = "0" + selectedMonth;

                event.setMonth(selectedMonth);
                event.setYear(selectedYear);
                event.setTime(selectedTime);
                event.setRepeatMode("D");

                if (isUpdating) {
                    event.setId(Integer.parseInt(eventId));
                    eventDao.updateEvent(event);
                    Toast.makeText(this, "Event Updated", Toast.LENGTH_LONG).show();
                    onBackPressed();
                } else {
                    eventDao.insert(event);
                    Toast.makeText(this, "Event Added", Toast.LENGTH_LONG).show();
                    onBackPressed();
                }
            }
        }
        if (v == btnDeleteEvent) {
            eventDao.deleteEvent(eventId);
            Toast.makeText(this, "Event Deleted", Toast.LENGTH_LONG).show();
            onBackPressed();
        }
        if (v == btnSelectRepeatMode) {
            showDialogToSelectRepeatMode();
        }
    }

    private void showDialogToSelectRepeatMode() {
        SelectRepeatModeFragment frag = SelectRepeatModeFragment.newInstance(repeatMode);
        frag.show(getSupportFragmentManager(),"");
    }

    private void showTimePicker() {
        final Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(CreateEventActivity.this,
                (timePicker, selectedHour, selectedMinute) -> {
                    String format = "";
                    if (selectedHour > 12) {
                        format = "pm";
                        selectedHour -= 12;
                    } else {
                        format = "am";
                    }
                    selectedTime = selectedHour + ":" + selectedMinute + " " + format;

                    btnSelectTime.setText("selected Time: " + selectedTime);
                }, hour, minute, false);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void showDatePicker() {

        picker = new DatePickerDialog(CreateEventActivity.this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    monthOfYear++;
                    selectedMonth = String.valueOf(monthOfYear);
                    selectedYear = String.valueOf(year1);
                    selectedDay = String.valueOf(dayOfMonth);
                    setSelectedDate();
                }, Integer.valueOf(selectedYear), Integer.valueOf(selectedMonth) - 1, Integer.valueOf(selectedDay));
        picker.show();

    }

    private Boolean isFormValid() {
        if (etEvenName.getText().toString().trim().equals("")) {
            etEvenName.setError("Name Required");
            return false;
        }

        if (btnSelectDate.getText().toString().equalsIgnoreCase("Select Date")) {
            Toast.makeText(this, "Please Select date for your event", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }

    @Override
    public void onRepeatModeSelected(String repeatMode) {

    }
}







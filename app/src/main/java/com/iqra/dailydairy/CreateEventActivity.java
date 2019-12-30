package com.iqra.dailydairy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.iqra.dailydairy.room.EventDao;
import com.iqra.dailydairy.room.MyRoomDatabase;

import java.util.Calendar;

public class CreateEventActivity<AddReminder> extends AppCompatActivity implements View.OnClickListener {


    TextView btnSelectDate, btnSelectTime, btnSaveEvent, customDialog;
    EditText etEvenName, etVenue, etNote;
    DatePickerDialog picker;
    String selectedYear, selectedDay, selectedMonth, selectedTime = "";
    EventDao eventDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        eventDao = MyRoomDatabase.getDatabase(this).eventDao();
        initComponents();
    }


    private void initComponents() {
        btnSelectDate = findViewById(R.id.btnSelectDate);
        btnSelectTime = findViewById(R.id.btnSelectTime);
        btnSaveEvent = findViewById(R.id.btnSaveEvent);
        btnSelectDate.setOnClickListener(this);
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

                eventDao.insert(event);
                Toast.makeText(this, "Event Added", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showTimePicker() {
        final Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(CreateEventActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String format = "";
                        if (selectedHour > 12) {
                            format = "pm";
                            selectedHour -= 12;
                        } else {
                            format = "am";
                        }
                        selectedTime = selectedHour + ":" + selectedMinute + " " + format;

                        btnSelectTime.setText("selected Time: " + selectedTime);
                    }
                }, hour, minute, false);
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }


    private void showDatePicker() {
        final Calendar cldr = Calendar.getInstance();
        int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);

        // date picker dialog
        picker = new DatePickerDialog(CreateEventActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        monthOfYear++;
                        selectedMonth = String.valueOf(monthOfYear);
                        selectedYear = String.valueOf(year);
                        selectedDay = String.valueOf(dayOfMonth);

                        btnSelectDate.setText("Selected Date:  " + dayOfMonth + "-" + monthOfYear + "-" + year);
                    }
                }, year, month, day);
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
}







package com.iqra.dailydairy;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class CreateEventActivity extends AppCompatActivity implements View.OnClickListener {

    TextView btnSelectDate,btnSelectTime;
    DatePickerDialog picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        initComponents();
    }

    private void initComponents() {
        btnSelectDate = findViewById(R.id.btnSelectDate);
        btnSelectTime = findViewById(R.id.btnSelectTime);
        btnSelectDate.setOnClickListener(this);
        btnSelectTime.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnSelectDate) {
            showDatePicker();
        }if (v == btnSelectTime) {
            showTimePicker();
        }
    }

    private void showTimePicker() {
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
                     btnSelectDate.setText("Selected Date: " + dayOfMonth + "-" + monthOfYear + "-" + year);
                    }
                }, year, month, day);
        picker.show();
    }
}




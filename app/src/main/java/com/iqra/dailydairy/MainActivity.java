package com.iqra.dailydairy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.iqra.dailydairy.utils.MonthProvider;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton btnNextMonth, btnPreviousMonth;
    TextView tvSelectedMonth;
    ArrayList<String> months;
    Calendar calendar;
    Date currentDate;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
    }

    private void initComponents() {
        btnNextMonth = findViewById(R.id.btnNextMonth);
        btnPreviousMonth = findViewById(R.id.btnLastMonth);
        btnNextMonth.setOnClickListener(this);
        btnPreviousMonth.setOnClickListener(this);
        tvSelectedMonth = findViewById(R.id.tvSelectedMonth);

        calendar = Calendar.getInstance();
        currentDate = calendar.getTime();
        tvSelectedMonth.setText(formatDate(currentDate));
    }

    @Override
    public void onClick(View v) {
        if (v == btnNextMonth) {
            nextMonth();
        }
        if (v == btnPreviousMonth) {
            previousMonth();
        }
    }

    private void nextMonth() {
        calendar.add(Calendar.MONTH, 1);
        currentDate = calendar.getTime();
        tvSelectedMonth.setText(formatDate(currentDate));
    }

    private void previousMonth() {
        calendar.add(Calendar.MONTH, -1);
        currentDate = calendar.getTime();
        tvSelectedMonth.setText(formatDate(currentDate));
    }

    private String formatDate(Date date) {
        SimpleDateFormat df = new SimpleDateFormat("MMM-yyyy");
        return df.format(date);
    }
}
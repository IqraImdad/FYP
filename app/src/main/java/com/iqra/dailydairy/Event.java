package com.iqra.dailydairy;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity
public class Event {
    private String name;
    private String venue;
    private String note;
    private String day;
    private String month;
    private String year;
    private String time;
    private Boolean isDeleted = false;
    private Boolean isMoreThenOne = false;
    private String repeatMode;


    public Boolean getMoreThenOne() {
        return isMoreThenOne;
    }

    public void setMoreThenOne(Boolean moreThenOne) {
        isMoreThenOne = moreThenOne;
    }

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(@NonNull int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRepeatMode() {
        return repeatMode;
    }

    public void setRepeatMode(String repeatMode) {
        this.repeatMode = repeatMode;
    }

    public Date getDate() {
        Date date;
        String dtStart;
        SimpleDateFormat format;
        if (getTime().equalsIgnoreCase("")) {
            dtStart = getYear() + "-" + getMonth() + "-" + getDay();
            format = new SimpleDateFormat("yyyy-MM-d", Locale.getDefault());
        } else {
            dtStart = getYear() + "-" + getMonth() + "-" + getDay() + "-" + getTime();
            format = new SimpleDateFormat("yyyy-MM-dd-hh:mm aa", Locale.getDefault());
        }
        try {
            date = format.parse(dtStart);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return date;
    }


    public String getlastaction() {
        return day + "-" + month + "-" + year;
    }


    @Override
    public boolean equals(Object v) {
        boolean retVal = false;

        if (v instanceof Event){
            Event ptr = (Event) v;
            retVal = ptr.id == this.id;
        }

        return retVal;
    }

}

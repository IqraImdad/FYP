package com.iqra.dailydairy;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


import java.util.ArrayList;

@Entity
public class Chain {

   @PrimaryKey(autoGenerate = true)
   private int id;

    private String name;

    private ArrayList<Event> events;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }


}




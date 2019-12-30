package com.iqra.dailydairy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.iqra.dailydairy.adapters.NewChainEventsAdapter;
import com.iqra.dailydairy.room.EventDao;
import com.iqra.dailydairy.room.MyRoomDatabase;

import java.util.ArrayList;

public class CreateNewChainActivity extends AppCompatActivity {

    RecyclerView rvEvents;
    EventDao eventDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_chain);

        eventDao = MyRoomDatabase.getDatabase(this).eventDao();


        initComponents();
        buildRecyclerView();

    }

    private void initComponents() {
        rvEvents = findViewById(R.id.rvEvents);
    }

    private void buildRecyclerView() {
        rvEvents.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<Event> events =  (ArrayList<Event>) eventDao.getEvents();

        NewChainEventsAdapter adapter = new NewChainEventsAdapter(events);
        rvEvents.setAdapter(adapter);

    }
}

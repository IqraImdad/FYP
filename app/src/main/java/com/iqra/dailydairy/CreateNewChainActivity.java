package com.iqra.dailydairy;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iqra.dailydairy.adapters.NewChainEventsAdapter;
import com.iqra.dailydairy.room.ChainDao;
import com.iqra.dailydairy.room.EventDao;
import com.iqra.dailydairy.room.MyRoomDatabase;

import java.util.ArrayList;

public class CreateNewChainActivity extends AppCompatActivity implements OnCheckBoxClicked, View.OnClickListener {

    RecyclerView rvEvents;
    Button btnSaveChain;
    EditText etChainName;
    EventDao eventDao;
    ChainDao chainDao;
    ArrayList<Event> selectedEvents;
    ArrayList<Event> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_chain);

        eventDao = MyRoomDatabase.getDatabase(this).eventDao();
        chainDao = MyRoomDatabase.getDatabase(this).chainDao();
        selectedEvents = new ArrayList<>();
        initComponents();
        buildRecyclerView();

    }

    private void initComponents() {
        rvEvents = findViewById(R.id.rvEvents);
        btnSaveChain = findViewById(R.id.btnSaveChain);
        etChainName = findViewById(R.id.etChainName);

        btnSaveChain.setOnClickListener(this);
    }

    private void buildRecyclerView() {
        rvEvents.setLayoutManager(new LinearLayoutManager(this));
        events = (ArrayList<Event>) eventDao.getEvents();

        NewChainEventsAdapter adapter = new NewChainEventsAdapter(events);
        adapter.setCheckBoxClickListener(this);
        rvEvents.setAdapter(adapter);

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

            } else if(etChainName.getText().toString().trim().equalsIgnoreCase("")){
                etChainName.setError("Name Required");
            }else {
                Chain chain = new Chain();
                chain.setName(etChainName.getText().toString().trim());
                chain.setEvents(selectedEvents);
                chainDao.insert(chain);
                Toast.makeText(this, "Chain saved", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

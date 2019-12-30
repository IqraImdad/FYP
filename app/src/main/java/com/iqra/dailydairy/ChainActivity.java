package com.iqra.dailydairy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.iqra.dailydairy.adapters.ChainsAdapter;
import com.iqra.dailydairy.room.ChainDao;
import com.iqra.dailydairy.room.MyRoomDatabase;

import java.util.ArrayList;

public class ChainActivity extends AppCompatActivity implements View.OnClickListener , OnItemClicked {

    FloatingActionButton fabAddEvent;
    ArrayList<Chain> chainsList;
    ChainDao chainDao;
    RecyclerView rvChains;

    public static ArrayList<Event> selectedChainsEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chain);
        chainDao = MyRoomDatabase.getDatabase(this).chainDao();
        chainsList = (ArrayList<Chain>) chainDao.getChains();
        selectedChainsEvents = new ArrayList<>();
        initComponents();
        buildRecyclerView();
    }

    private void buildRecyclerView() {
        rvChains.setLayoutManager(new LinearLayoutManager(this));
        ChainsAdapter adapter = new ChainsAdapter(chainsList);
        rvChains.setAdapter(adapter);
        adapter.setOnClickListener(this);
    }

    private void initComponents() {

        fabAddEvent = findViewById(R.id.fabAddEvent);
        fabAddEvent.setOnClickListener(this);

        rvChains = findViewById(R.id.rvChains);
    }

    @Override
    public void onClick(View view) {
        if(view == fabAddEvent)
        {
            startActivity(new Intent(this,CreateNewChainActivity.class));
        }
    }

    @Override
    public void onItemClicked(int position) {
        selectedChainsEvents = chainsList.get(position).getEvents();
        startActivity(new Intent(this,ChainsEventActivity.class));
    }
}

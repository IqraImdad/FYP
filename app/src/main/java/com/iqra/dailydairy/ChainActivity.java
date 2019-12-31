package com.iqra.dailydairy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.iqra.dailydairy.adapters.ChainsAdapter;
import com.iqra.dailydairy.room.ChainDao;
import com.iqra.dailydairy.room.MyRoomDatabase;

import java.util.ArrayList;

public class ChainActivity extends AppCompatActivity implements View.OnClickListener, OnItemClicked {

    FloatingActionButton fabAddEvent;
    TextView tvNd;
    ArrayList<Chain> chainsList;
    ChainDao chainDao;
    RecyclerView rvChains;

    public static ArrayList<Event> selectedChainsEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chain);
        initComponents();

    }

    @Override
    protected void onResume() {
        super.onResume();
        chainDao = MyRoomDatabase.getDatabase(this).chainDao();
        chainsList = (ArrayList<Chain>) chainDao.getChains();
        selectedChainsEvents = new ArrayList<>();
        if (chainsList.size() < 1) {
            tvNd.setVisibility(View.VISIBLE);
        } else {
            tvNd.setVisibility(View.GONE);
            buildRecyclerView();
        }
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
        tvNd = findViewById(R.id.tvNd);
    }

    @Override
    public void onClick(View view) {
        if (view == fabAddEvent) {
            startActivity(new Intent(this, CreateNewChainActivity.class));
        }
    }

    @Override
    public void onItemClicked(int position) {
        selectedChainsEvents = chainsList.get(position).getEvents();
        startActivity(new Intent(this, ChainsEventActivity.class));
    }
}

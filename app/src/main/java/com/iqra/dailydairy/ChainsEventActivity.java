package com.iqra.dailydairy;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iqra.dailydairy.adapters.ChainsEventsAdapter;

public class ChainsEventActivity extends AppCompatActivity {

    RecyclerView rvChainsEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chains_event);

        initComponents();
        buildRecyclerView();
    }

    private void buildRecyclerView() {
        rvChainsEvent.setLayoutManager(new LinearLayoutManager(this));
        ChainsEventsAdapter adapter = new ChainsEventsAdapter(ChainActivity.selectedChainsEvents);
        rvChainsEvent.setAdapter(adapter);
    }

    private void initComponents() {
        rvChainsEvent = findViewById(R.id.rvChainsEvents);
    }
}

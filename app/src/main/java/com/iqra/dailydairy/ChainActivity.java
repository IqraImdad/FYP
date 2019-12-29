package com.iqra.dailydairy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ChainActivity extends AppCompatActivity implements View.OnClickListener {

    FloatingActionButton fabAddEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chain);
        
        initComponents();
    }

    private void initComponents() {

        fabAddEvent = findViewById(R.id.fabAddEvent);
        fabAddEvent.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == fabAddEvent)
        {
            
        }
    }
}

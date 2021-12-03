package com.example.mqttcontrol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> controllers;

    private RecyclerView controllerRecycle;
    private FloatingActionButton addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        controllerRecycle = findViewById(R.id.controllerRecycle);
        addButton = findViewById(R.id.addButton);

        controllers = new ArrayList<String>();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controllers.add(UUID.randomUUID().toString().replaceAll("-", ""));
                renderRecycler(view);
            }
        });
        // Just to maintain the default render cycle.
        //renderRecycler();
    }

    private void renderRecycler(View view){
        DpadAdapter adapter = new DpadAdapter(controllers);
        controllerRecycle.setAdapter(adapter);
        controllerRecycle.setLayoutManager(new LinearLayoutManager(this));
    }
}
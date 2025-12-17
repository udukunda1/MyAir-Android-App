package com.example.myair;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Apply padding only to top bar for status bar
        View topBar = findViewById(R.id.top_bar);
        ViewCompat.setOnApplyWindowInsetsListener(topBar, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), systemBars.top, v.getPaddingRight(), v.getPaddingBottom());
            return insets;
        });

        // Set up click listener for Manage Passengers button
        LinearLayout btnManagePassengers = findViewById(R.id.btn_uber);
        btnManagePassengers.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PassengerActivity.class);
            startActivity(intent);
        });
    }
}
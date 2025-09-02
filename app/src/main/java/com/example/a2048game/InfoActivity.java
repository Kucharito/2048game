package com.example.a2048game;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class InfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_activity);

        Button backButton = findViewById(R.id.buttonBackToMenu);
        backButton.setOnClickListener(v -> {
            // Return to main menu
            Intent intent = new Intent(InfoActivity.this, MainMenuActivity.class);
            startActivity(intent);
        });
    }

}

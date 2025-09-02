package com.example.a2048game;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class OptionsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options_activity);

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            // Return to main menu
            Intent intent = new Intent(OptionsActivity.this, MainMenuActivity.class);
            startActivity(intent);
        });
    }
}

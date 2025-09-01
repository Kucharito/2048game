package com.example.a2048game;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainMenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_menu), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button startButton = findViewById(R.id.buttonStart);
        startButton.setOnClickListener(v -> {
            // Start the game activity
            Intent intent = new Intent(MainMenuActivity.this, MainActivity.class);
            startActivity(intent);
        });

        Button infoButton = findViewById(R.id.buttonInfo);
        infoButton.setOnClickListener(v -> {
            // Show game information
            Intent intent = new Intent(MainMenuActivity.this, InfoActivity.class);
            startActivity(intent);
        });
        Button optionsButton = findViewById(R.id.buttonOptions);
        optionsButton.setOnClickListener(v -> {
            // Show game options
            Intent intent = new Intent(MainMenuActivity.this, OptionsActivity.class);
            startActivity(intent);
        });
        Button exitButton = findViewById(R.id.buttonExit);
        exitButton.setOnClickListener(v -> {
            // Exit the application
            finishAffinity();
        });

    }
}

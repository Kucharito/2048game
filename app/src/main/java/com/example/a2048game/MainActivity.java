package com.example.a2048game;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.Grid;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private TextView[][] cells = new TextView[4][4];
    private GridLayout gridLayout;
    private TextView scoreText;
    private TextView highestScoreText;
    private Game2048 game;
    private LinearLayout menuOverlay;
    private int highest_score = 0;
    private LinearLayout gameOverOverlay;
    private LinearLayout gameWinOverlay;
    private SoundPool soundPool;
    private AudioAttributes audioAttributes;
    private int soundSlideTile;
    private boolean soundsReady = false;
    private float downX, downY;


    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "sharedPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        gridLayout = findViewById(R.id.grid2048);
        scoreText = findViewById(R.id.scoreText);
        highestScoreText = findViewById(R.id.bestScoreText);
        game = new Game2048();

        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        highest_score = sharedPreferences.getInt("highest_score", 0);

        audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_GAME)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(5)
                .setAudioAttributes(audioAttributes)
                .build();

        soundSlideTile = soundPool.load(this, R.raw.swipe, 1);

        soundPool.setOnLoadCompleteListener((soundPool, sampleId, status) -> {
            if (status == 0) {
                soundsReady = true;
            } else {
                // Handle the error
            }
        });


        initGrid();
        game.startGame();
        updateUI();

        menuOverlay = findViewById(R.id.menuOverlay);
        gameOverOverlay = findViewById(R.id.gameOverOverlay);
        gameWinOverlay = findViewById(R.id.gameWinOverlay);

        Button openMenuButton = findViewById(R.id.openMenuButton);
        Button resumeButton = findViewById(R.id.resumeButton);
        Button mainMenuButton = findViewById(R.id.mainMenuButton);
        Button restartButton = findViewById(R.id.restartButton);
        Button mainMenuButton_second = findViewById(R.id.mainMenuButton_second);
        Button mainMenuButton_third = findViewById(R.id.mainMenuButton_third);
        Button restartButton_second = findViewById(R.id.restartButton_second);

        openMenuButton.setOnClickListener(v -> {
            menuOverlay.setVisibility(View.VISIBLE);
        });

        resumeButton.setOnClickListener(v -> {
            menuOverlay.setVisibility(View.GONE);
        });


        mainMenuButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MainMenuActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            //finish();
        });

        mainMenuButton_second.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MainMenuActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            //finish();
        });

        mainMenuButton_third.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MainMenuActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            //finish();
        });

        restartButton.setOnClickListener(v -> {
            game.startGame();
            updateUI();
            menuOverlay.setVisibility(View.GONE);
            gameOverOverlay.setVisibility(View.GONE);
            gameWinOverlay.setVisibility(View.GONE);
        });

        restartButton_second.setOnClickListener(v -> {
            game.startGame();
            updateUI();
            menuOverlay.setVisibility(View.GONE);
            gameOverOverlay.setVisibility(View.GONE);
            gameWinOverlay.setVisibility(View.GONE);
        });

    }

    private void playHit(){
        if(soundsReady){
            soundPool.play(soundSlideTile, 1,1,0,0,1);
        }
    }

    @Override
    public boolean onTouchEvent(android.view.MotionEvent event) {
        switch (event.getAction()){
            case android.view.MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                return true;
            case android.view.MotionEvent.ACTION_UP:
                float upX = event.getX();
                float upY = event.getY();
                float deltaX = upX - downX;
                float deltaY = upY - downY;
                boolean moved = false;
                if(Math.abs(deltaX) > Math.abs(deltaY)){
                    if(deltaX >100){
                        moved = game.moveRight();
                    } else if(deltaX < -100){
                        moved = game.moveLeft();
                    }
                } else{
                    if(deltaY >100){
                        moved = game.moveDown();
                    } else if(deltaY < -100){
                        moved = game.moveUp();
                    }
                }
                if(moved){
                    updateUI();
                    playHit();
                }
                return true;
        }
        return super.onTouchEvent(event);
    }


    private void initGrid(){
        int childIndex = 0;
        for (int i=0;i <4;i++){
            for ( int j =0;j<4;j++){
                TextView cell = new TextView(this);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = 0;
                params.height = 0;
                params.rowSpec = GridLayout.spec(i,1f);
                params.columnSpec = GridLayout.spec(j,1f);
                params.setMargins(8,8,8,8);

                cell.setLayoutParams(params);
                cell.setGravity(android.view.Gravity.CENTER);
                cell.setTextSize(32);
                cell.setTypeface(Typeface.DEFAULT_BOLD);
                cell.setBackgroundColor(Color.parseColor("#CDC1B4"));
                cell.setText("");

                gridLayout.addView(cell);
                cells[i][j] = cell;
                childIndex++;
            }
        }
    }

    private void updateUI(){

        int[][] grid = game.getGrid();
        for (int i=0;i<4;i++) {
            for (int j = 0; j < 4; j++) {
                int value = grid[i][j];
                TextView cell = cells[i][j];
                if(value == 0){
                    cell.setText("");
                    cell.setBackgroundColor(Color.parseColor("#CDC1B4"));
                }
                else{
                    /*cell.setText(String.valueOf(value));
                    cell.setBackgroundColor(getCellColor(value));*/
                        String oldText = cell.getText().toString();
                        if(cell.getText().toString().isEmpty()){
                            cell.setAlpha(0f);
                            cell.setText(String.valueOf(value));
                            cell.setBackgroundColor(getCellColor(value));
                            cell.animate().alpha(1f).setDuration(150).start();
                        }
                        else if(!oldText.equals(String.valueOf(value))){
                            cell.setScaleX(0.8f);
                            cell.setScaleY(0.8f);
                            cell.setText(String.valueOf(value));
                            cell.setBackgroundColor(getCellColor(value));
                            cell.animate().scaleX(1f).scaleY(1f).setDuration(150).start();

                        }
                        else{
                            cell.setText(String.valueOf(value));
                            cell.setBackgroundColor(getCellColor(value));
                        }
                }
            }
        }
        if(highest_score < game.getScore()){
            highest_score = game.getScore();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("highest_score", highest_score);
            editor.apply();
        }


        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                if(grid[i][j] == 2048){
                    gameWinOverlay.setVisibility(View.VISIBLE);
                    menuOverlay.setVisibility(View.GONE);
                    gameOverOverlay.setVisibility(View.GONE);
                }
            }

        }



        scoreText.setText(getString(R.string.Score) + game.getScore());
        highestScoreText.setText(getString(R.string.BestScore) + highest_score);

        if(game.isGameOver()){
            LinearLayout gameOverOverlay = findViewById(R.id.gameOverOverlay);
            gameOverOverlay.setVisibility(View.VISIBLE);
        }

    }
    private int getCellColor(int value){
        // Implement color logic based on tile value
        switch(value){
            case 2: return Color.parseColor("#EEE4DA");
            case 4: return Color.parseColor("#EDE0C8");
            case 8: return Color.parseColor("#F2B179");
            case 16: return Color.parseColor("#F59563");
            case 32: return Color.parseColor("#F67C5F");
            case 64: return Color.parseColor("#F65E3B");
            case 128: return Color.parseColor("#EDCF72");
            case 256: return Color.parseColor("#EDCC61");
            case 512: return Color.parseColor("#EDC850");
            case 1024: return Color.parseColor("#EDC53F");
            case 2048: return Color.parseColor("#EDC22E");
            default: return Color.parseColor("#3C3A32");
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            gridLayout.setSystemUiVisibility(
                    android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | android.view.View.SYSTEM_UI_FLAG_FULLSCREEN
                            | android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

}
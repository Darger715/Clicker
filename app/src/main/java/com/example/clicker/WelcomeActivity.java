package com.example.clicker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class WelcomeActivity extends AppCompatActivity {

    TextView textView_levelInfo;
    TextView textView_scoreInfo;
    int level = 1;
    int score;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activiy_screen);

        textView_levelInfo = findViewById(R.id.welcomeActivity_textView_levelInfo);
        textView_scoreInfo = findViewById(R.id.welcomeActivity_textView_scoreInfo);
        findViewById(R.id.welcomeActivity_button_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                textView_levelInfo.setText("Level:" + level);
                textView_scoreInfo.setText("Score: " + score);


                Intent intent_game_field_acivity = new Intent(WelcomeActivity.this, GameFieldActivity.class);
                intent_game_field_acivity.putExtra("levelInfo_intent", level);
                intent_game_field_acivity.putExtra("scoreInfo_intent", score);
                startActivity(intent_game_field_acivity);
            }
        });


    }
}

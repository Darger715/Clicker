package com.example.clicker;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class WelcomeActivity extends AppCompatActivity {

    TextView textView_levelInfo;
    TextView textView_scoreInfo;
    Button update_results;
    int level = 1;
    int score;

    final int REQUEST_CODE_SCORE = 1;
    final int REQUEST_CODE_LEVEL = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activiy_screen);

        textView_levelInfo = findViewById(R.id.welcomeActivity_textView_levelInfo);
        textView_scoreInfo = findViewById(R.id.welcomeActivity_textView_scoreInfo);
        update_results = findViewById(R.id.welcomeActivity_button_update_results);

        textView_levelInfo.setText("Level:" + level);
        textView_scoreInfo.setText("Score: " + score);



        findViewById(R.id.welcomeActivity_button_update_results).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent_game_field_acivity = new Intent(WelcomeActivity.this, GameFieldActivity.class);
                intent_game_field_acivity.putExtra("levelInfo_intent", level);
                intent_game_field_acivity.putExtra("scoreInfo_intent", score);
                           startActivity(intent_game_field_acivity);}
        });



        update_results.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent new_score_intent = new Intent(WelcomeActivity.this, GameFieldActivity.class);
                startActivityForResult(new_score_intent, REQUEST_CODE_SCORE);

                //Intent new_level_intent = new Intent(WelcomeActivity.this, GameFieldActivity.class);
                //startActivityForResult(new_level_intent, REQUEST_CODE_LEVEL);
            }
        });


    }
    protected void OnActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_LEVEL:
                    level = data.getIntExtra("new_level", score);
                    textView_levelInfo.setText("Level:" + level);
                    Log.e("SCORE","NEW SCORE");
                    break;
                case REQUEST_CODE_SCORE:
                    level = data.getIntExtra("new_level", 1);
                    textView_scoreInfo.setText("Score: " + score);
                    break;
            }
        } else {
            Toast.makeText(WelcomeActivity.this, "WRONG", Toast.LENGTH_SHORT).show();
        }
    }
}
